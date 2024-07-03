package banking.management.controller;

import banking.management.dto.LoanAccountDTO;
import banking.management.model.LoanStatus;
import banking.management.response.APIResponse;
import banking.management.service.LoanService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

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

        when(loanService.createLoan(any(LoanAccountDTO.class))).thenReturn(loanAccountDTO);

        ResponseEntity<APIResponse<LoanAccountDTO>> response = loanController.createLoan(loanAccountDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.CREATED, response.getBody().getMeta().getStatusCode());
        assertEquals("Loan created successfully", response.getBody().getMeta().getDisplayMessage());
        assertEquals(loanAccountDTO, response.getBody().getData());
        verify(loanService, times(1)).createLoan(any(LoanAccountDTO.class));
    }

    @Test
    public void testCreateLoan_AccountNotFound() {
        LoanAccountDTO loanAccountDTO = new LoanAccountDTO();
        loanAccountDTO.setAccountId(1L);

        when(loanService.createLoan(any(LoanAccountDTO.class))).thenThrow(new EntityNotFoundException("Account not found"));

        ResponseEntity<APIResponse<LoanAccountDTO>> response = loanController.createLoan(loanAccountDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getMeta().getStatusCode());
        assertEquals("Account not found", response.getBody().getMeta().getDisplayMessage());
        assertNull(response.getBody().getData());
        verify(loanService, times(1)).createLoan(any(LoanAccountDTO.class));
    }

    @Test
    public void testGetLoanById_Success() {
        LoanAccountDTO loanAccountDTO = new LoanAccountDTO();
        loanAccountDTO.setLoanId(1L);
        loanAccountDTO.setAccountId(1L);
        loanAccountDTO.setLoanType("Home");
        loanAccountDTO.setLoanAmount(5000L);
        loanAccountDTO.setLoanStatus(LoanStatus.PAID);
        loanAccountDTO.setRemainingAmount(3000L);
        loanAccountDTO.setMonthlyEMI(500);

        when(loanService.getLoanById(anyLong())).thenReturn(loanAccountDTO);

        ResponseEntity<APIResponse<LoanAccountDTO>> response = loanController.getLoanById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.OK, response.getBody().getMeta().getStatusCode());
        assertEquals("Loan retrieved successfully", response.getBody().getMeta().getDisplayMessage());
        assertEquals(loanAccountDTO, response.getBody().getData());
        verify(loanService, times(1)).getLoanById(anyLong());
    }

    @Test
    public void testGetLoanById_LoanNotFound() {
        when(loanService.getLoanById(anyLong())).thenThrow(new EntityNotFoundException("Loan not found"));

        ResponseEntity<APIResponse<LoanAccountDTO>> response = loanController.getLoanById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getMeta().getStatusCode());
        assertEquals("Loan not found", response.getBody().getMeta().getDisplayMessage());
        assertNull(response.getBody().getData());
        verify(loanService, times(1)).getLoanById(anyLong());
    }

    @Test
    public void testUpdateLoan_Success() {
        LoanAccountDTO loanAccountDTO = new LoanAccountDTO();
        loanAccountDTO.setLoanId(1L);
        loanAccountDTO.setAccountId(1L);
        loanAccountDTO.setLoanType("Home");
        loanAccountDTO.setLoanAmount(5000L);
        loanAccountDTO.setLoanStatus(LoanStatus.PAID);
        loanAccountDTO.setRemainingAmount(3000L);
        loanAccountDTO.setMonthlyEMI(500);

        when(loanService.updateLoan(anyLong(), any(LoanAccountDTO.class))).thenReturn(loanAccountDTO);

        ResponseEntity<APIResponse<LoanAccountDTO>> response = loanController.updateLoan(1L, loanAccountDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.OK, response.getBody().getMeta().getStatusCode());
        assertEquals("Loan updated successfully", response.getBody().getMeta().getDisplayMessage());
        assertEquals(loanAccountDTO, response.getBody().getData());
        verify(loanService, times(1)).updateLoan(anyLong(), any(LoanAccountDTO.class));
    }

    @Test
    public void testUpdateLoan_LoanNotFound() {
        LoanAccountDTO loanAccountDTO = new LoanAccountDTO();
        loanAccountDTO.setLoanId(1L);

        when(loanService.updateLoan(anyLong(), any(LoanAccountDTO.class))).thenThrow(new EntityNotFoundException("Loan not found"));

        ResponseEntity<APIResponse<LoanAccountDTO>> response = loanController.updateLoan(1L, loanAccountDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getMeta().getStatusCode());
        assertEquals("Loan not found", response.getBody().getMeta().getDisplayMessage());
        assertNull(response.getBody().getData());
        verify(loanService, times(1)).updateLoan(anyLong(), any(LoanAccountDTO.class));
    }

    @Test
    public void testDeleteLoan_Success() {
        doNothing().when(loanService).deleteLoan(anyLong());

        ResponseEntity<APIResponse<Void>> response = loanController.deleteLoan(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(loanService, times(1)).deleteLoan(anyLong());
    }

    @Test
    public void testDeleteLoan_LoanNotFound() {
        doThrow(new EntityNotFoundException("Loan not found")).when(loanService).deleteLoan(anyLong());

        ResponseEntity<APIResponse<Void>> response = loanController.deleteLoan(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getMeta().getStatusCode());
        assertEquals("Loan not found", response.getBody().getMeta().getDisplayMessage());
        assertNull(response.getBody().getData());
        verify(loanService, times(1)).deleteLoan(anyLong());
    }
}
