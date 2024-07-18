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
    ResponseMeta meta = new ResponseMeta();

    @PostMapping("/create")
    public ResponseEntity<APIResponse<LoanAccountDTO>> createLoan(@Valid @RequestBody LoanAccountDTO loanAccountDTO) {
        APIResponse<LoanAccountDTO> apiResponse = new APIResponse<>();

        try {
            logger.info("Creating new loan...");
            LoanAccountDTO createdLoan = loanService.createLoan(loanAccountDTO);
            logger.info("Loan created successfully with ID: " + createdLoan.getLoanId());

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.CREATED);
            meta.setDisplayMessage("Loan created successfully");

            apiResponse.setMeta(meta);
            apiResponse.setData(createdLoan);

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
            logger.log(Level.SEVERE, "Data integrity violation during loan creation: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            meta.setDisplayMessage("Data integrity violation");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the loan: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<APIResponse<LoanAccountDTO>> getLoanById(@PathVariable Long loanId) {
        APIResponse<LoanAccountDTO> apiResponse = new APIResponse<>();

        try {
            logger.info("Retrieving loan with ID: " + loanId);
            LoanAccountDTO loanDTO = loanService.getLoanById(loanId);
            logger.info("Loan retrieved successfully for ID: " + loanId);

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.OK);
            meta.setDisplayMessage("Loan retrieved successfully");

            apiResponse.setMeta(meta);
            apiResponse.setData(loanDTO);

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Loan not found with ID: " + loanId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("Loan not found");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the loan: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{loanId}")
    public ResponseEntity<APIResponse<LoanAccountDTO>> updateLoan(@PathVariable Long loanId, @Valid @RequestBody LoanAccountDTO loanAccountDTO) {
        APIResponse<LoanAccountDTO> apiResponse = new APIResponse<>();

        try {
            logger.info("Updating loan with ID: " + loanId);
            LoanAccountDTO updatedLoan = loanService.updateLoan(loanId, loanAccountDTO);
            logger.info("Loan updated successfully with ID: " + updatedLoan.getLoanId());

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.OK);
            meta.setDisplayMessage("Loan updated successfully");

            apiResponse.setMeta(meta);
            apiResponse.setData(updatedLoan);

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Loan not found with ID: " + loanId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("Loan not found");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during loan update: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            meta.setDisplayMessage("Data integrity violation");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the loan: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<APIResponse<Void>> deleteLoan(@PathVariable Long loanId) {
        APIResponse<Void> apiResponse = new APIResponse<>();

        try {
            logger.info("Deleting loan with ID: " + loanId);
            loanService.deleteLoan(loanId);
            logger.info("Loan deleted successfully with ID: " + loanId);

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.NO_CONTENT);
            meta.setDisplayMessage("Loan deleted successfully");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Loan not found with ID: " + loanId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("Loan not found");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the loan: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");

            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
