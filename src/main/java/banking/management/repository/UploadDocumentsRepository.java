package banking.management.repository;

import banking.management.model.UploadDocuments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface UploadDocumentsRepository extends ElasticsearchRepository<UploadDocuments,String> {

}
