package banking.management.controller;

import banking.management.dto.PaymentAccountDTO;
import banking.management.response.APIResponse;
import banking.management.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidPayment_whenCreatePayment_thenReturnsCreated() {
        PaymentAccountDTO paymentAccountDTO = new PaymentAccountDTO();
        paymentAccountDTO.setPaymentId(1L);

        when(paymentService.createPayment(any(PaymentAccountDTO.class))).thenReturn(paymentAccountDTO);

        ResponseEntity<APIResponse<PaymentAccountDTO>> response = paymentController.createPayment(paymentAccountDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMeta());
        assertTrue(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.CREATED, response.getBody().getMeta().getStatusCode());
        assertEquals("Payment created successfully", response.getBody().getMeta().getDisplayMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(1L, response.getBody().getData().getPaymentId());
        verify(paymentService, times(1)).createPayment(any(PaymentAccountDTO.class));
    }

    @Test
    public void givenEntityNotFoundException_whenCreatePayment_thenReturnsNotFound() {
        when(paymentService.createPayment(any(PaymentAccountDTO.class)))
                .thenThrow(new EntityNotFoundException("Account not found"));

        ResponseEntity<APIResponse<PaymentAccountDTO>> response = paymentController.createPayment(new PaymentAccountDTO());

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMeta());
        assertFalse(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getMeta().getStatusCode());
        assertEquals("Account not found", response.getBody().getMeta().getDisplayMessage());
        verify(paymentService, times(1)).createPayment(any(PaymentAccountDTO.class));
    }

    @Test
    public void givenValidPaymentId_whenGetPaymentById_thenReturnsPaymentDTO() {
        PaymentAccountDTO paymentAccountDTO = new PaymentAccountDTO();
        paymentAccountDTO.setPaymentId(1L);

        when(paymentService.getPaymentById(anyLong())).thenReturn(paymentAccountDTO);

        ResponseEntity<APIResponse<PaymentAccountDTO>> response = paymentController.getPaymentById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMeta());
        assertTrue(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.OK, response.getBody().getMeta().getStatusCode());
        assertEquals("Payment retrieved successfully", response.getBody().getMeta().getDisplayMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(1L, response.getBody().getData().getPaymentId());
        verify(paymentService, times(1)).getPaymentById(anyLong());
    }

    @Test
    public void givenPaymentIdNotFound_whenGetPaymentById_thenReturnsNotFound() {
        when(paymentService.getPaymentById(anyLong()))
                .thenThrow(new EntityNotFoundException("Payment not found"));

        ResponseEntity<APIResponse<PaymentAccountDTO>> response = paymentController.getPaymentById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMeta());
        assertFalse(response.getBody().getMeta().isSuccess());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getMeta().getStatusCode());
        assertEquals("Payment not found", response.getBody().getMeta().getDisplayMessage());
        verify(paymentService, times(1)).getPaymentById(anyLong());
    }
}
