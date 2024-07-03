package banking.management.controller;

import banking.management.dto.PaymentAccountDTO;
import banking.management.response.APIResponse;
import banking.management.response.ResponseMeta;
import banking.management.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());

    @PostMapping("/create")
    public ResponseEntity<APIResponse<PaymentAccountDTO>> createPayment(@Valid @RequestBody PaymentAccountDTO paymentAccountDTO) {
        try {
            logger.info("Creating new payment...");
            PaymentAccountDTO createdPayment = paymentService.createPayment(paymentAccountDTO);
            logger.info("Payment created successfully with ID: " + createdPayment.getPaymentId());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.CREATED)
                    .displayMessage("Payment created successfully")
                    .build();

            APIResponse<PaymentAccountDTO> apiResponse = APIResponse.<PaymentAccountDTO>builder()
                    .meta(meta)
                    .data(createdPayment)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Entity not found: " + e.getMessage(), e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Account not found")
                    .build();

            APIResponse<PaymentAccountDTO> apiResponse = APIResponse.<PaymentAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during payment creation: " + e.getMessage());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_ACCEPTABLE)
                    .displayMessage("Data integrity violation")
                    .build();

            APIResponse<PaymentAccountDTO> apiResponse = APIResponse.<PaymentAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the payment: " + e.getMessage(), e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<PaymentAccountDTO> apiResponse = APIResponse.<PaymentAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<APIResponse<PaymentAccountDTO>> getPaymentById(@PathVariable Long paymentId) {
        try {
            logger.info("Retrieving payment with ID: " + paymentId);
            PaymentAccountDTO paymentAccountDTO = paymentService.getPaymentById(paymentId);
            logger.info("Payment retrieved successfully for ID: " + paymentId);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.OK)
                    .displayMessage("Payment retrieved successfully")
                    .build();

            APIResponse<PaymentAccountDTO> apiResponse = APIResponse.<PaymentAccountDTO>builder()
                    .meta(meta)
                    .data(paymentAccountDTO)
                    .build();

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Payment not found with ID: " + paymentId, e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Payment not found")
                    .build();

            APIResponse<PaymentAccountDTO> apiResponse = APIResponse.<PaymentAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the payment: " + e.getMessage(), e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<PaymentAccountDTO> apiResponse = APIResponse.<PaymentAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
