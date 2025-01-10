package banking.management.service;

import banking.management.dto.PaymentAccountDTO;
import banking.management.model.Account;
import banking.management.model.Payment1;
import banking.management.repository.AccountRepository;
import banking.management.repository.PaymentRepository;
import banking.management.util.AccountContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = Logger.getLogger(PaymentServiceImpl.class.getName());

    @Override
    @Transactional
    public PaymentAccountDTO createPayment(PaymentAccountDTO paymentAccountDTO) {
        Payment1 savedPayment = null;
        try {
            logger.info("Creating a new payment...");

            AccountContext.setCurrentAccountId(paymentAccountDTO.getAccountId());

            Payment1 payment = new Payment1();
            payment.setPaymentType(paymentAccountDTO.getPaymentType());
            payment.setAmountWithdrawn(paymentAccountDTO.getAmountWithdrawn());
            payment.setAmountDeposited(paymentAccountDTO.getAmountDeposited());

            Optional<Account> account1 = accountRepository.findByAccountId(paymentAccountDTO.getAccountId());
            Account account2 = account1.orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + paymentAccountDTO.getAccountId()));
            payment.setAccount(account2);
            payment.setAccountBalance(account2.getCurrentBalance() - paymentAccountDTO.getAmountWithdrawn() + paymentAccountDTO.getAmountDeposited());
            payment.setTransactionDate(LocalDate.now());
            account2.setCurrentBalance(payment.getAccountBalance());

            List<Payment1> payments = account2.getPayments();
            payments.add(payment);
            account2.setPayments(payments);

            savedPayment = paymentRepository.save(payment);
            logger.info("Payment created successfully with ID: " + savedPayment.getPaymentId());
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage());
            throw new EntityNotFoundException("Entity not found with ID: " + paymentAccountDTO.getAccountId());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the payment: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while creating the payment");
        }finally {
            AccountContext.clear();
        }
        return mapToDTO(savedPayment);
    }

    @Override
    @Transactional
    public PaymentAccountDTO getPaymentById(Long paymentId) {
        try {
            logger.info("Retrieving payment with ID: " + paymentId);
            Payment1 payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + paymentId));
            logger.info("Payment retrieved successfully for ID: " + paymentId);
            return mapToDTO(payment);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage());
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the payment: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while retrieving the payment");
        }
    }

    @Transactional
    @Override
    public List<Payment1> getPaymentHistory(Long paymentId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Payment1.class, paymentId);

        if (revisions == null || revisions.isEmpty()) {
            return null;
        }

        List<Payment1> paymentHistory = new ArrayList<>();
        for (Number revision : revisions) {
            Payment1 payment = auditReader.find(Payment1.class, paymentId, revision);
            if (payment != null) {
                paymentHistory.add(payment);
            }
        }

        return paymentHistory;
    }


    private PaymentAccountDTO mapToDTO(Payment1 payment) {
        PaymentAccountDTO dto = new PaymentAccountDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setPaymentType(payment.getPaymentType());
        dto.setAccountId(payment.getAccount().getAccountId());
        dto.setAmountWithdrawn(payment.getAmountWithdrawn());
        dto.setAmountDeposited(payment.getAmountDeposited());

        return dto;
    }
}
