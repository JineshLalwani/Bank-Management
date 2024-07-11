package banking.management.service;

import banking.management.dto.AccountDetailsDTO;
import banking.management.model.Account;
import banking.management.model.AccountType;
import banking.management.model.Details;
import banking.management.model.User;
import banking.management.repository.AccountRepository;
import banking.management.repository.DetailsRepository;
import banking.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private DetailsRepository detailsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccount_Success() {

        AccountDetailsDTO dto = new AccountDetailsDTO();
        String ifsc = "ICIC0000009";
        dto.setAccountType(AccountType.SAVINGS);
        dto.setCurrentBalance(1000);
        dto.setAccountStatus("Active");
        dto.setMonthlyInterest(1.5F);
        dto.setBankIfsc(ifsc);
        dto.setUserId(Arrays.asList(1L, 2L));

        Details details = new Details();
        details.setBankIfsc(ifsc);

        User user1 = new User();
        user1.setUserId(1L);
        User user2 = new User();
        user2.setUserId(2L);

        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrentBalance(1000);
        account.setAccountStatus("Active");
        account.setMonthlyInterest(1.5F);
        account.setDateOpened(LocalDate.now());
        account.setDetails(details);

        List<User> users = Arrays.asList(user1, user2);
        account.setUsers(users);

        when(detailsRepository.findByBankIfsc(ifsc)).thenReturn(Optional.of(details));
        when(userRepository.findByUserIdIn(Arrays.asList(1L, 2L))).thenReturn(users);
        when(accountRepository.save(any(Account.class))).thenReturn(account);


        AccountDetailsDTO result = accountService.createAccount(dto);


        assertNotNull(account.getAccountId());
        assertNotNull(result);
        assertEquals(account.getAccountType(), result.getAccountType());
        assertEquals(account.getCurrentBalance(), result.getCurrentBalance());
        assertEquals(account.getAccountStatus(), result.getAccountStatus());
        assertEquals(account.getMonthlyInterest(), result.getMonthlyInterest());
        assertEquals(account.getDetails().getBankIfsc(), result.getBankIfsc());
        assertEquals(Arrays.asList(1L, 2L), result.getUserId());
    }


    @Test
    public void testCreateAccount_DetailsNotFound() {

        AccountDetailsDTO dto = new AccountDetailsDTO();
        dto.setAccountId(1L);
        dto.setBankIfsc("ICIC0000010");

        when(detailsRepository.findByBankIfsc("ICIC0000010")).thenReturn(Optional.empty());


        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.createAccount(dto);
        });
        assertEquals("Details not found for IFSC: ICIC0000010", exception.getMessage());
    }

    @Test
    public void testGetAccountById_Success() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrentBalance(1000);
        account.setAccountStatus("Active");
        account.setMonthlyInterest(1.5F);
        account.setDateOpened(LocalDate.now());

        Details details = new Details();
        details.setBankIfsc("ICIC0000009");
        account.setDetails(details);

        User user1 = new User();
        user1.setUserId(1L);
        User user2 = new User();
        user2.setUserId(2L);
        account.setUsers(Arrays.asList(user1, user2));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        AccountDetailsDTO result = accountService.getAccountById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(account.getAccountType(), result.getAccountType());
        assertEquals(account.getCurrentBalance(), result.getCurrentBalance());
        assertEquals(account.getAccountStatus(), result.getAccountStatus());
        assertEquals(account.getMonthlyInterest(), result.getMonthlyInterest());
        assertEquals(account.getDetails().getBankIfsc(), result.getBankIfsc());
        assertEquals(Arrays.asList(1L, 2L), result.getUserId());
    }

    @Test
    public void testGetAccountById_AccountNotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountById(1L);
        });
        assertEquals("Account not found with ID: 1", exception.getMessage());
    }


    @Test
    public void testUpdateAccount_Success() {
        // Arrange
        AccountDetailsDTO dto = new AccountDetailsDTO();
        dto.setAccountType(AccountType.SAVINGS);
        dto.setCurrentBalance(1500);
        dto.setAccountStatus("Active");
        dto.setMonthlyInterest(2.0F);
        dto.setBankIfsc("ICIC0000009");
        dto.setUserId(Arrays.asList(1L, 2L));

        Details details = new Details();
        details.setBankIfsc("ICIC0000009");

        User user1 = new User();
        user1.setUserId(1L);
        User user2 = new User();
        user2.setUserId(2L);

        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrentBalance(1000);
        account.setAccountStatus("Active");
        account.setMonthlyInterest(1.5F);
        account.setDateOpened(LocalDate.now());
        account.setDetails(details);
        account.setUsers(Arrays.asList(user1, user2));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(detailsRepository.findByBankIfsc("ICIC0000009")).thenReturn(Optional.of(details));
        when(userRepository.findByUserIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(user1, user2));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        AccountDetailsDTO result = accountService.updateAccount(1L, dto);

        // Assert
        assertNotNull(result);
        assertEquals(account.getAccountType(), result.getAccountType());
        assertEquals(account.getCurrentBalance(), result.getCurrentBalance());
        assertEquals(account.getAccountStatus(), result.getAccountStatus());
        assertEquals(account.getMonthlyInterest(), result.getMonthlyInterest());
        assertEquals(account.getDetails().getBankIfsc(), result.getBankIfsc());
        assertEquals(Arrays.asList(1L, 2L), result.getUserId());
    }

    @Test
    public void testDeleteAccount_Success() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(true);

        // Act
        accountService.deleteAccount(accountId);

        // Assert
        verify(accountRepository, times(1)).deleteById(accountId);
    }

    @Test
    public void testDeleteAccount_AccountNotFound() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.deleteAccount(accountId);
        });
        assertEquals("Account not found with ID: 1", exception.getMessage());
    }
}
