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

    private APIResponse<PaymentAccountDTO> apiResponse = new APIResponse<>();
    private ResponseMeta meta = new ResponseMeta();

    @PostMapping("/create")
    public ResponseEntity<APIResponse<PaymentAccountDTO>> createPayment(@Valid @RequestBody PaymentAccountDTO paymentAccountDTO) {

        try {
            logger.info("Creating new payment...");
            PaymentAccountDTO createdPayment = paymentService.createPayment(paymentAccountDTO);
            logger.info("Payment created successfully with ID: " + createdPayment.getPaymentId());

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.CREATED);
            meta.setDisplayMessage("Payment created successfully");

            apiResponse.setMeta(meta);
            apiResponse.setData(createdPayment);

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Entity not found: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("Account not found");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during payment creation: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            meta.setDisplayMessage("Data integrity violation");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the payment: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<APIResponse<PaymentAccountDTO>> getPaymentById(@PathVariable Long paymentId) {

        try {
            logger.info("Retrieving payment with ID: " + paymentId);
            PaymentAccountDTO paymentAccountDTO = paymentService.getPaymentById(paymentId);
            logger.info("Payment retrieved successfully for ID: " + paymentId);

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.OK);
            meta.setDisplayMessage("Payment retrieved successfully");

            apiResponse.setMeta(meta);
            apiResponse.setData(paymentAccountDTO);

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Payment not found with ID: " + paymentId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("Payment not found");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the payment: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
