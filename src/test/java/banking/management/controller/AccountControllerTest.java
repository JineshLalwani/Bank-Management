package banking.management.controller;

import banking.management.dto.AccountDetailsDTO;
import banking.management.model.AccountType;
import banking.management.response.APIResponse;
import banking.management.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccount_Success() {

        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
        accountDetailsDTO.setAccountType(AccountType.SAVINGS);
        accountDetailsDTO.setCurrentBalance(1000);
        accountDetailsDTO.setAccountStatus("Active");
        accountDetailsDTO.setMonthlyInterest(1.5F);
        accountDetailsDTO.setBankIfsc("IFSC001");
        accountDetailsDTO.setUserId(Arrays.asList(1L, 2L));

        when(accountService.createAccount(any(AccountDetailsDTO.class))).thenReturn(accountDetailsDTO);

        ResponseEntity<APIResponse<AccountDetailsDTO>> response = accountController.createAccount(accountDetailsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(accountDetailsDTO, response.getBody().getData());
        assertTrue(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testCreateAccount_Failure() {
        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();

        when(accountService.createAccount(any(AccountDetailsDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<APIResponse<AccountDetailsDTO>> response = accountController.createAccount(accountDetailsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testGetAccountById_Success() {
        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
        accountDetailsDTO.setAccountId(1L);
        accountDetailsDTO.setAccountType(AccountType.SAVINGS);
        accountDetailsDTO.setCurrentBalance(1000);
        accountDetailsDTO.setAccountStatus("Active");
        accountDetailsDTO.setMonthlyInterest(1.5F);
        accountDetailsDTO.setBankIfsc("IFSC001");
        accountDetailsDTO.setUserId(Arrays.asList(1L, 2L));

        when(accountService.getAccountById(anyLong())).thenReturn(accountDetailsDTO);

        ResponseEntity<APIResponse<AccountDetailsDTO>> response = accountController.getAccountById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(accountDetailsDTO, response.getBody().getData());
        assertTrue(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testGetAccountById_NotFound() {
        when(accountService.getAccountById(anyLong())).thenThrow(new EntityNotFoundException("Account not found with ID: 1"));

        ResponseEntity<APIResponse<AccountDetailsDTO>> response = accountController.getAccountById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testUpdateAccount_Success() {
        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
        accountDetailsDTO.setAccountId(1L);
        accountDetailsDTO.setAccountType(AccountType.SAVINGS);
        accountDetailsDTO.setCurrentBalance(1500);
        accountDetailsDTO.setAccountStatus("Active");
        accountDetailsDTO.setMonthlyInterest(2.0F);
        accountDetailsDTO.setBankIfsc("IFSC001");
        accountDetailsDTO.setUserId(Arrays.asList(1L, 2L));

        when(accountService.updateAccount(anyLong(), any(AccountDetailsDTO.class))).thenReturn(accountDetailsDTO);

        ResponseEntity<APIResponse<AccountDetailsDTO>> response = accountController.updateAccount(1L, accountDetailsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(accountDetailsDTO, response.getBody().getData());
        assertTrue(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testUpdateAccount_NotFound() {
        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();

        when(accountService.updateAccount(anyLong(), any(AccountDetailsDTO.class))).thenThrow(new EntityNotFoundException("Account not found with ID: 1"));

        ResponseEntity<APIResponse<AccountDetailsDTO>> response = accountController.updateAccount(1L, accountDetailsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testDeleteAccount_Success() {
        doNothing().when(accountService).deleteAccount(anyLong());

        ResponseEntity<APIResponse<Void>> response = accountController.deleteAccount(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertTrue(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testDeleteAccount_NotFound() {
        doThrow(new EntityNotFoundException("Account not found with ID: 1")).when(accountService).deleteAccount(anyLong());

        ResponseEntity<APIResponse<Void>> response = accountController.deleteAccount(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().getMeta().isSuccess());
    }
}
