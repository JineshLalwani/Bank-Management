package banking.management.service;

import banking.management.dto.PaymentAccountDTO;
import banking.management.model.Loan;
import banking.management.model.Payment;
import banking.management.model.Account;
import banking.management.model.PaymentType;
import banking.management.repository.LoanRepository;
import banking.management.repository.PaymentRepository;
import banking.management.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Autowired
    private LoanRepository loanRepository;

    private static final Logger logger = Logger.getLogger(PaymentServiceImpl.class.getName());

    @Override
    @Transactional
    public PaymentAccountDTO createPayment(PaymentAccountDTO paymentAccountDTO) {
        Payment savedPayment = null;
        try {
            logger.info("Creating a new payment...");

            Payment payment = new Payment();
            payment.setPaymentType(paymentAccountDTO.getPaymentType());
            payment.setAmountWithdrawn(paymentAccountDTO.getAmountWithdrawn());
            payment.setAmountDeposited(paymentAccountDTO.getAmountDeposited());

            Optional<Account> account1 = accountRepository.findByAccountId(paymentAccountDTO.getAccountId());
            Account account2 = account1.orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + paymentAccountDTO.getAccountId()));
            payment.setAccount(account2);
            payment.setAccountBalance(account2.getCurrentBalance() - paymentAccountDTO.getAmountWithdrawn() + paymentAccountDTO.getAmountDeposited());
            payment.setTransactionDate(LocalDate.now());
            account2.setCurrentBalance(payment.getAccountBalance());

            List<Payment> payments = account2.getPayments();
            payments.add(payment);
            account2.setPayments(payments);

            savedPayment = paymentRepository.save(payment);
            logger.info("Payment created successfully with ID: " + savedPayment.getPaymentId());
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the payment: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while creating the payment");
        }
        return mapToDTO(savedPayment);
    }

    @Override
    @Transactional
    public PaymentAccountDTO getPaymentById(Long paymentId) {
        try {
            logger.info("Retrieving payment with ID: " + paymentId);
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + paymentId));
            logger.info("Payment retrieved successfully for ID: " + paymentId);
            return mapToDTO(payment);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the payment: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while retrieving the payment");
        }
    }

    // Helper method to map Payment entity to PaymentAccountDTO
    private PaymentAccountDTO mapToDTO(Payment payment) {
        PaymentAccountDTO dto = new PaymentAccountDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setPaymentType(payment.getPaymentType());
        dto.setAccountId(payment.getAccount().getAccountId());
        dto.setAmountWithdrawn(payment.getAmountWithdrawn());
        dto.setAmountDeposited(payment.getAmountDeposited());
        // Map other properties as needed
        return dto;
    }
}
