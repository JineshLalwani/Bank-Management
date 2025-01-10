package banking.management.repository;

import banking.management.model.Payment1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment1, Long> {
}
