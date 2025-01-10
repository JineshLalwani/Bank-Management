package banking.management.repository;


import banking.management.model.BankDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Integer> {
    Page<BankDetails> findAll(Pageable pageable);
}
