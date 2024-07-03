package banking.management.service;

import banking.management.dto.LoanAccountDTO;
import banking.management.model.Account;
import banking.management.model.Loan;
import banking.management.model.LoanStatus;
import banking.management.repository.AccountRepository;
import banking.management.repository.LoanRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLoan_Success() {

        LoanAccountDTO loanAccountDTO = new LoanAccountDTO();
        loanAccountDTO.setAccountId(1L);
        loanAccountDTO.setLoanType("Home");
        loanAccountDTO.setLoanAmount(5000L);
        loanAccountDTO.setLoanStatus(LoanStatus.PAID);
        loanAccountDTO.setRemainingAmount(3000L);
        loanAccountDTO.setMonthlyEMI(500);

        Account account = new Account();
        account.setAccountId(1L);
        account.setCurrentBalance(2000);
        account.setDateOpened(LocalDate.now());
        account.setLoans(new ArrayList<>()); // Initialize the loans list

        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanType("Home");
        loan.setLoanAmount(5000L);
        loan.setLoanStatus(LoanStatus.PAID);
        loan.setRemainingAmount(3000L);
        loan.setMonthlyEMI(500);
        loan.setDateOpened(LocalDate.now());
        loan.setAccount(account);

        List<Loan> loanList = account.getLoans();
        loanList.add(loan);
        account.setLoans(loanList);

        when(accountRepository.findByAccountId(anyLong())).thenReturn(Optional.of(account));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // Act
        LoanAccountDTO result = loanService.createLoan(loanAccountDTO);

        // Assert
        assertNotNull(result);
        assertEquals(loan.getLoanId(), result.getLoanId());
        assertEquals(loan.getAccount().getAccountId(), result.getAccountId());
        assertEquals(loan.getLoanType(), result.getLoanType());
        assertEquals(loan.getLoanAmount(), result.getLoanAmount());
        assertEquals(loan.getLoanStatus(), result.getLoanStatus());
        assertEquals(loan.getRemainingAmount(), result.getRemainingAmount());
        assertEquals(loan.getMonthlyEMI(), result.getMonthlyEMI());
        verify(accountRepository, times(1)).findByAccountId(anyLong());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }


    @Test
    public void testGetLoanById_Success() {
        // Arrange
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanType("Home");
        loan.setLoanAmount(5000L);
        loan.setLoanStatus(LoanStatus.PAID);
        loan.setRemainingAmount(3000L);
        loan.setMonthlyEMI(500);
        loan.setDateOpened(LocalDate.now());

        Account account = new Account();
        account.setAccountId(1L);
        loan.setAccount(account);

        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));

        // Act
        LoanAccountDTO result = loanService.getLoanById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(loan.getLoanId(), result.getLoanId());
        assertEquals(loan.getAccount().getAccountId(), result.getAccountId());
        assertEquals(loan.getLoanType(), result.getLoanType());
        assertEquals(loan.getLoanAmount(), result.getLoanAmount());
        assertEquals(loan.getLoanStatus(), result.getLoanStatus());
        assertEquals(loan.getRemainingAmount(), result.getRemainingAmount());
        assertEquals(loan.getMonthlyEMI(), result.getMonthlyEMI());
        verify(loanRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetLoanById_LoanNotFound() {
        // Arrange
        when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            loanService.getLoanById(1L);
        });
        assertEquals("Loan not found with ID: 1", exception.getMessage());
        verify(loanRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdateLoan_Success() {
        LoanAccountDTO loanAccountDTO = new LoanAccountDTO();
        loanAccountDTO.setAccountId(1L);
        loanAccountDTO.setLoanType("Home");
        loanAccountDTO.setLoanAmount(5000L);
        loanAccountDTO.setLoanStatus(LoanStatus.PAID);
        loanAccountDTO.setRemainingAmount(3000L);
        loanAccountDTO.setMonthlyEMI(500);

        Account account = new Account();
        account.setAccountId(1L);
        account.setCurrentBalance(2000);
        account.setDateOpened(LocalDate.now());
        account.setLoans(new ArrayList<>());

        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanType("Home");
        loan.setLoanAmount(5000L);
        loan.setLoanStatus(LoanStatus.PAID);
        loan.setRemainingAmount(3000L);
        loan.setMonthlyEMI(500);
        loan.setDateOpened(LocalDate.now());
        loan.setAccount(account);

        List<Loan> loanList = account.getLoans();
        loanList.add(loan);
        account.setLoans(loanList);

        when(accountRepository.findByAccountId(anyLong())).thenReturn(Optional.of(account));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // Act
        LoanAccountDTO result = loanService.createLoan(loanAccountDTO);

        // Assert
        assertNotNull(result);
        assertEquals(loan.getLoanId(), result.getLoanId());
        assertEquals(loan.getAccount().getAccountId(), result.getAccountId());
        assertEquals(loan.getLoanType(), result.getLoanType());
        assertEquals(loan.getLoanAmount(), result.getLoanAmount());
        assertEquals(loan.getLoanStatus(), result.getLoanStatus());
        assertEquals(loan.getRemainingAmount(), result.getRemainingAmount());
        assertEquals(loan.getMonthlyEMI(), result.getMonthlyEMI());
        verify(accountRepository, times(1)).findByAccountId(anyLong());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    public void testUpdateLoan_LoanNotFound() {
        // Arrange
        LoanAccountDTO loanAccountDTO = new LoanAccountDTO();
        loanAccountDTO.setAccountId(1L);

        when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            loanService.updateLoan(1L, loanAccountDTO);
        });
        assertEquals("Loan not found with ID: 1", exception.getMessage());
        verify(loanRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(0)).findByAccountId(anyLong());
        verify(loanRepository, times(0)).save(any(Loan.class));
    }

    @Test
    public void testDeleteLoan_Success() {
        // Arrange
        when(loanRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(loanRepository).deleteById(anyLong());

        // Act
        loanService.deleteLoan(1L);

        // Assert
        verify(loanRepository, times(1)).existsById(anyLong());
        verify(loanRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteLoan_LoanNotFound() {
        // Arrange
        when(loanRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            loanService.deleteLoan(1L);
        });
        assertEquals("Loan not found with ID: 1", exception.getMessage());
        verify(loanRepository, times(1)).existsById(anyLong());
        verify(loanRepository, times(0)).deleteById(anyLong());
    }
}
