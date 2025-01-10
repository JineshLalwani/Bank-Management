package banking.management.service;

import banking.management.model.BankDetails;
import banking.management.model.BankDetailsElasticSearch;
import banking.management.repository.BankDetailsElasticSearchRepository;
import banking.management.repository.BankDetailsRepository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.snapshot.CreateSnapshotRequest;
import co.elastic.clients.elasticsearch.snapshot.CreateSnapshotResponse;
import co.elastic.clients.elasticsearch.snapshot.DeleteSnapshotRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.reindex.ReindexResponse;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import org.springframework.data.elasticsearch.core.reindex.ReindexRequest;

@Service
@Slf4j
public class MigrateServiceImpl implements MigrateService {

    @Autowired
    private BankDetailsRepository bankDetailsRepository;

    @Autowired
    private BankDetailsElasticSearchRepository elasticSearchRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int BATCH_SIZE = 100;

    @Override
    @Transactional
    public void migrateData() throws IOException {
//        elasticsearchClient.indices().delete(d -> d
//                .index("bank_master")
//        );
        int pageNumber = 0;
        Page<BankDetails> page;

        do {
            page = bankDetailsRepository.findAll(PageRequest.of(pageNumber, BATCH_SIZE));
            List<BankDetailsElasticSearch> userDocuments = new ArrayList<>();

            for (BankDetails bankDetails : page.getContent()) {
                BankDetailsElasticSearch bankMasterElasticSearch = new BankDetailsElasticSearch();
                bankMasterElasticSearch.setId(bankDetails.getId());
                bankMasterElasticSearch.setIfsc(bankDetails.getIfsc());
                bankMasterElasticSearch.setBranch(bankDetails.getBranch());
                bankMasterElasticSearch.setMicr(bankDetails.getMicr());
                bankMasterElasticSearch.setContact(bankDetails.getContact());
                bankMasterElasticSearch.setUpi(bankDetails.isUpi());
                bankMasterElasticSearch.setRtgs(bankDetails.isRtgs());
                bankMasterElasticSearch.setNeft(bankDetails.isNeft());
                bankMasterElasticSearch.setImps(bankDetails.isImps());
                bankMasterElasticSearch.setSwift(bankDetails.getSwift());
                bankMasterElasticSearch.setIso3166(bankDetails.getIso3166());
                bankMasterElasticSearch.setBank(bankDetails.getBank());
                bankMasterElasticSearch.setBankCode(bankDetails.getBankCode());
                bankMasterElasticSearch.setCity(bankDetails.getCity());
                bankMasterElasticSearch.setCentre(bankDetails.getCentre());
                bankMasterElasticSearch.setState(bankDetails.getState());
                bankMasterElasticSearch.setDistrict(bankDetails.getDistrict());
                bankMasterElasticSearch.setAddress(bankDetails.getAddress());
                bankMasterElasticSearch.setCreatedAt((Date.from((bankDetails.getCreatedAt()).toInstant())));
                bankMasterElasticSearch.setUpdatedAt((Date.from((bankDetails.getUpdatedAt()).toInstant())));

                userDocuments.add(bankMasterElasticSearch);
            }

            elasticSearchRepository.saveAll(userDocuments);

            pageNumber++;
        } while (page.hasNext());
    }

    @Override
    public Iterable<BankDetailsElasticSearch> findByBranch(String name){
        elasticSearchRepository.deleteAll();
        return elasticSearchRepository.findByBranch(name);

    }

    @Override
    public Object getDocumentsMigrate(String keyword) throws IOException {
        try {
            String searchTerm=keyword.toLowerCase();
            SearchResponse<BankDetailsElasticSearch> response = elasticsearchClient.search(s -> s
                            .index("bank_master")
                            .query(q -> q.multiMatch(m -> m
                                    .query(searchTerm)
                                    .fields("branch", "ifsc"))),
                    BankDetailsElasticSearch.class);

            List<BankDetailsElasticSearch> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();


//            SearchResponse<BankDetailsElasticSearch> response1 = elasticsearchClient.search(s -> s
//                            .index("bank_master")
//                            .query(q -> q.wildcard(m -> m
//                                    .field("ifsc")
//                                    .value("*"+searchTerm+"*"))),
//                    BankDetailsElasticSearch.class);

            SearchResponse<BankDetailsElasticSearch> response3 = elasticsearchClient.search(s -> s
                            .index("bank_master")
                            .query(q -> q.bool(b -> b
                                    .should(q1 -> q1.match(m -> m.field("ifsc")
                                            .query("*"+keyword+"*")
                                            .fuzziness("AUTO")
                                    ))
                                    .should(q2 -> q2.match(m -> m.field("micr")
                                            .query("*"+keyword+"*")
                                            .fuzziness("AUTO")
                                    ))
                                    .should(q3 -> q3.match(m -> m.field("address")
                                            .query("*"+keyword+"*")
                                            .fuzziness("AUTO")
                                    ))
                                    .should(q4 -> q4.match(m -> m.field("branch")
                                            .query("*"+keyword+"*")
                                            .fuzziness("AUTO")
                                    ))
                            ))
                            .minScore(0.7),
                    BankDetailsElasticSearch.class
            );

            List<BankDetailsElasticSearch> results2 = response3.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

            SearchResponse<BankDetailsElasticSearch> response2 = elasticsearchClient.search(s -> s
                            .index("bank_master")
                            .query(q -> q.bool(b -> b
                                    .should(q1 -> q1.wildcard(m -> m.field("ifsc").value("*" + searchTerm + "*")))
                                    .should(q2 -> q2.wildcard(m -> m.field("micr").value("*" + searchTerm + "*")))
                                    .should(q3 -> q3.wildcard(m -> m.field("address").value("*" + searchTerm + "*")))
                                    .should(q3 -> q3.wildcard(m -> m.field("branch").value("*" + searchTerm + "*")))
                            )),
                    BankDetailsElasticSearch.class
            );

            List<BankDetailsElasticSearch> results1 = response2.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
            return results;
        } catch (ElasticsearchException e) {
            log.error("Elasticsearch query failed: {}", e.getMessage());
            throw new IOException("Elasticsearch query failed", e);
        }

    }
    @Override
    public void reindex(String sourceIndex, String destinationIndex) {
        IndexCoordinates source = IndexCoordinates.of(sourceIndex);
        IndexCoordinates destination = IndexCoordinates.of(destinationIndex);
        ReindexRequest request = new ReindexRequest.ReindexRequestBuilder(source, destination)
                .build();
        ReindexResponse response = elasticsearchOperations.reindex(request);
        System.out.println("Reindex response details: " + response);
    }

    @Override
    public void createIndexWithSettingsAndMappings(String indexName) throws JsonProcessingException {
        String json = """
                {
                       "settings": {
                         "index.max_ngram_diff": 8,
                         "analysis": {
                           "normalizer": {
                             "normalizer1": {
                               "type": "custom",
                               "char_filter": [],
                               "filter": ["lowercase"]
                             }
                           },
                           "tokenizer": {
                             "ngram_tokenizer": {
                               "type": "ngram",
                               "min_gram": 2,
                               "max_gram": 10,
                               "token_chars": [
                                 "letter",
                                 "digit"
                               ]
                             }
                           },
                           "analyzer": {
                             "ngram_analyzer": {
                               "type": "custom",
                               "tokenizer": "ngram_tokenizer",
                               "filter": [
                                 "lowercase"
                               ]
                             }
                           }
                         }
                       },
                       "mappings": {
                         "properties": {
                           "ifsc": {
                             "type": "keyword",
                             "normalizer": "normalizer1"
                           },
                           "micr": {
                             "type": "keyword",
                             "normalizer": "normalizer1"
                           },
                           "contact": {
                             "type": "keyword",
                             "normalizer": "normalizer1"
                           },
                           "branch": {
                             "type": "text",
                             "analyzer": "ngram_analyzer",
                             "search_analyzer": "standard"
                           },
                           "bank": {
                             "type": "text",
                             "analyzer": "ngram_analyzer",
                             "search_analyzer": "standard"
                           },
                           "address": {
                             "type": "text",
                             "analyzer": "ngram_analyzer",
                             "search_analyzer": "standard"
                           },
                           "city": {
                             "type": "text",
                             "analyzer": "ngram_analyzer",
                             "search_analyzer": "standard"
                           },
                           "state": {
                             "type": "text",
                             "analyzer": "ngram_analyzer",
                             "search_analyzer": "standard"
                           },
                           "district": {
                             "type": "text",
                             "analyzer": "ngram_analyzer",
                             "search_analyzer": "standard"
                           },
                           "centre": {
                             "type": "text",
                             "analyzer": "ngram_analyzer",
                             "search_analyzer": "standard"
                           }
                         }
                       }
                     }
            """;

//        Map<String, Object> indexConfig = objectMapper.readValue(json, Map.class);
//        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
//        if (elasticsearchOperations.indexOps(indexCoordinates).exists()) {
//            elasticsearchOperations.indexOps(indexCoordinates).delete();
//        }
//
//        boolean isCreated = elasticsearchOperations.indexOps(indexCoordinates).create(indexConfig);
//        System.out.println("Index created: " + isCreated);
        // Convert the JSON string to a Map (or Document) using ObjectMapper
        // Convert the JSON string to a Map
        Map<String, Object> indexConfig = objectMapper.readValue(json, Map.class);
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

        // Check if the index already exists and delete if necessary
        if (elasticsearchOperations.indexOps(indexCoordinates).exists()) {
            elasticsearchOperations.indexOps(indexCoordinates).delete();
        }

        // Create index settings and mappings from the parsed map
        Map<String, Object> settings = (Map<String, Object>) indexConfig.get("settings");
        Map<String, Object> mappings = (Map<String, Object>) indexConfig.get("mappings");

        // Create index with settings and mappings
        boolean isCreated = elasticsearchOperations.indexOps(indexCoordinates)
                .create(
                        Document.from(settings),
                        Document.from(mappings)
                );

        System.out.println("Index created: " + isCreated);
    }

    @Override
    public CreateSnapshotResponse createSnapshot(String repositoryName, String snapshotName) throws Exception {
        CreateSnapshotRequest request = new CreateSnapshotRequest.Builder()
                .indices("bank_master")
                .repository(repositoryName)
                .snapshot(snapshotName)
                .build();

        return elasticsearchClient.snapshot().create(request);
    }
//    public RestoreSnapshotResponse restoreSnapshot(String repositoryName, String snapshotName) throws Exception {
//        RestoreSnapshotRequest request = new RestoreSnapshotRequest.Builder()
//                .indices("bank_master")
//                .repository(repositoryName)
//                .snapshot(snapshotName)
//                .build();
//
//        return elasticsearchClient.snapshot().restore(request);
//    }

    public AcknowledgedResponse deleteSnapshot(String repositoryName, String snapshotName) throws Exception {
        DeleteSnapshotRequest request = new DeleteSnapshotRequest.Builder()
                .repository(repositoryName)
                .snapshot(snapshotName)
                .build();

        return elasticsearchClient.snapshot().delete(request);
    }

//    public SnapshotsStatusResponse getSnapshotsStatus(String repositoryName) throws Exception {
//        SnapshotsStatusRequest request = new SnapshotsStatusRequest.Builder()
//                .repository(repositoryName)
//                .build();
//
//        return elasticsearchClient.snapshot().status(request);
//    }
}
