package banking.management.service;

import banking.management.model.BankDetailsElasticSearch;
import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import co.elastic.clients.elasticsearch.snapshot.CreateSnapshotResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.io.IOException;

public interface MigrateService {
    @Transactional
    void migrateData() throws IOException;

    Iterable<BankDetailsElasticSearch> findByBranch(String name);

    Object getDocumentsMigrate(String keyword) throws IOException;

    void reindex(String sourceIndex, String destinationIndex);

    void createIndexWithSettingsAndMappings(String indexName) throws JsonProcessingException;

    CreateSnapshotResponse createSnapshot(String repositoryName, String snapshotName) throws Exception;
}
