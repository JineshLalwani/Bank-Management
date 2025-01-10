package banking.management.service;

import banking.management.model.UploadDocuments;
import banking.management.repository.UploadDocumentsRepository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.SimpleQueryStringQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UploadDocumentsServiceImpl implements UploadDocumentsService {

    @Autowired
    private UploadDocumentsRepository uploadDocumentsRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public UploadDocuments saveDocuments(UploadDocuments uploadDocuments) {
        return uploadDocumentsRepository.save(uploadDocuments);
    }

    @Override
    public Iterable<UploadDocuments> getDocuments() {
        return uploadDocumentsRepository.findAll();
    }

    @Override
    public Object getDocuments(String keyword) throws IOException {
        SearchResponse<UploadDocuments> response = elasticsearchClient.search(s -> s
                .index("upload_documents")
                .query(q -> q.multiMatch( m-> m
                        .query(keyword)
                        .fields("userName"))), UploadDocuments.class);

        List<UploadDocuments> results = response.hits().hits().stream()
                .map(Hit::source)
                .toList();

        Query doeContainsQuery = SimpleQueryStringQuery.of(q -> q.query(keyword+"*"))._toQuery();
        SearchResponse<UploadDocuments> response3 = elasticsearchClient.search(s -> s.query(q -> q.bool(b -> b
                .must(doeContainsQuery))), UploadDocuments.class);

        List<UploadDocuments> results1 = response3.hits().hits().stream()
                .map(Hit::source)
                .toList();
        response3.hits().hits().forEach(hit -> log.info("Response 3: {}", hit.source()));
        return results1;
    }
}
