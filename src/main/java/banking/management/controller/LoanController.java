package banking.management.controller;

import banking.management.dto.LoanAccountDTO;
import banking.management.response.APIResponse;
import banking.management.response.ResponseMeta;
import banking.management.service.LoanService;
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
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    private static final Logger logger = Logger.getLogger(LoanController.class.getName());

    @PostMapping("/create")
    public ResponseEntity<APIResponse<LoanAccountDTO>> createLoan(@Valid @RequestBody LoanAccountDTO loanAccountDTO) {
        try {
            logger.info("Creating new loan...");
            LoanAccountDTO createdLoan = loanService.createLoan(loanAccountDTO);
            logger.info("Loan created successfully with ID: " + createdLoan.getLoanId());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.CREATED)
                    .displayMessage("Loan created successfully")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(createdLoan)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Entity not found: " + e.getMessage(), e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Account not found")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during loan creation: " + e.getMessage());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_ACCEPTABLE)
                    .displayMessage("Data integrity violation")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the loan: " + e.getMessage(), e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<APIResponse<LoanAccountDTO>> getLoanById(@PathVariable Long loanId) {
        try {
            logger.info("Retrieving loan with ID: " + loanId);
            LoanAccountDTO loanDTO = loanService.getLoanById(loanId);
            logger.info("Loan retrieved successfully for ID: " + loanId);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.OK)
                    .displayMessage("Loan retrieved successfully")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(loanDTO)
                    .build();

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Loan not found with ID: " + loanId, e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Loan not found")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the loan: " + e.getMessage(), e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{loanId}")
    public ResponseEntity<APIResponse<LoanAccountDTO>> updateLoan(@PathVariable Long loanId, @Valid @RequestBody LoanAccountDTO loanAccountDTO) {
        try {
            logger.info("Updating loan with ID: " + loanId);
            LoanAccountDTO updatedLoan = loanService.updateLoan(loanId, loanAccountDTO);
            logger.info("Loan updated successfully with ID: " + updatedLoan.getLoanId());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.OK)
                    .displayMessage("Loan updated successfully")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(updatedLoan)
                    .build();

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Loan not found with ID: " + loanId, e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Loan not found")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during loan update: " + e.getMessage());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_ACCEPTABLE)
                    .displayMessage("Data integrity violation")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the loan: " + e.getMessage(), e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<LoanAccountDTO> apiResponse = APIResponse.<LoanAccountDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<APIResponse<Void>> deleteLoan(@PathVariable Long loanId) {
        try {
            logger.info("Deleting loan with ID: " + loanId);
            loanService.deleteLoan(loanId);
            logger.info("Loan deleted successfully with ID: " + loanId);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.NO_CONTENT)
                    .displayMessage("Loan deleted successfully")
                    .build();

            APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Loan not found with ID: " + loanId, e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Loan not found")
                    .build();

            APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the loan: " + e.getMessage(), e);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
