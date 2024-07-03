package banking.management.repository;

import banking.management.model.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Integer> {
    Optional<Details> findByBankIfsc(String bankIfsc);
}
