package banking.management.repository;

import banking.management.model.BankDetailsElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BankDetailsElasticSearchRepository extends ElasticsearchRepository<BankDetailsElasticSearch, String> {
    Iterable<BankDetailsElasticSearch> findByBranch(String branch);
}
