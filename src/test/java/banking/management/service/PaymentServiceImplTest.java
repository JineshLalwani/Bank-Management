package banking.management.service;

import banking.management.dto.PaymentAccountDTO;
import banking.management.model.Payment;
import banking.management.model.Account;
import banking.management.model.PaymentType;
import banking.management.repository.PaymentRepository;
import banking.management.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePayment_Success() {
        PaymentAccountDTO paymentAccountDTO = new PaymentAccountDTO();
        paymentAccountDTO.setAccountId(1L);
        paymentAccountDTO.setPaymentType(PaymentType.UPI);
        paymentAccountDTO.setAmountWithdrawn(0);
        paymentAccountDTO.setAmountDeposited(1000);

        Account account = new Account();
        account.setAccountId(1L);
        account.setCurrentBalance(500);
        account.setDateOpened(LocalDate.now());
        account.setPayments(new ArrayList<>());

        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setPaymentType(PaymentType.UPI);
        payment.setAmountWithdrawn(0);
        payment.setAmountDeposited(1000);
        payment.setAccount(account);
        payment.setAccountBalance(1500);
        payment.setTransactionDate(LocalDate.now());
        List<Payment> paymentList = account.getPayments();
        paymentList.add(payment);
        account.setPayments(paymentList);

        when(accountRepository.findByAccountId(anyLong())).thenReturn(Optional.of(account));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentAccountDTO result = paymentService.createPayment(paymentAccountDTO);

        assertNotNull(result);
        assertEquals(payment.getPaymentId(), result.getPaymentId());
        assertEquals(payment.getPaymentType(), result.getPaymentType());
        assertEquals(payment.getAmountDeposited(), result.getAmountDeposited());
        verify(accountRepository, times(1)).findByAccountId(anyLong());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void testCreatePayment_AccountNotFound() {
        PaymentAccountDTO paymentAccountDTO = new PaymentAccountDTO();
        paymentAccountDTO.setAccountId(1L);

        when(accountRepository.findByAccountId(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            paymentService.createPayment(paymentAccountDTO);
        });

        assertEquals("Account not found with ID: 1", exception.getMessage());
        verify(accountRepository, times(1)).findByAccountId(anyLong());
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    public void testGetPaymentById_Success() {
        Account account = new Account();
        account.setAccountId(1L);

        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setPaymentType(PaymentType.UPI);
        payment.setAmountWithdrawn(0);
        payment.setAmountDeposited(1000);
        payment.setAccountBalance(1500);
        payment.setTransactionDate(LocalDate.now());
        payment.setAccount(account);

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));

        PaymentAccountDTO result = paymentService.getPaymentById(1L);

        assertNotNull(result);
        assertEquals(payment.getPaymentId(), result.getPaymentId());
        assertEquals(payment.getPaymentType(), result.getPaymentType());
        assertEquals(payment.getAmountDeposited(), result.getAmountDeposited());
        verify(paymentRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetPaymentById_PaymentNotFound() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            paymentService.getPaymentById(1L);
        });

        assertEquals("Payment not found with ID: 1", exception.getMessage());
        verify(paymentRepository, times(1)).findById(anyLong());
    }
}
