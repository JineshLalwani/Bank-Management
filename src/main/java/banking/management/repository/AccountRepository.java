package banking.management.repository;

import banking.management.model.Account;
import banking.management.model.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
      Optional<Account> findByAccountId(Long accountId);
}
