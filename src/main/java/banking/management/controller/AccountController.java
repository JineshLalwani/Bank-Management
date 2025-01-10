package banking.management.controller;

import banking.management.dto.AccountDetailsDTO;
import banking.management.response.APIResponse;
import banking.management.response.ResponseMeta;
import banking.management.service.AccountService;
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
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    ResponseMeta meta = new ResponseMeta();

    private static final Logger logger = Logger.getLogger(AccountController.class.getName());

    @PostMapping
    public ResponseEntity<APIResponse<AccountDetailsDTO>> createAccount(@Valid @RequestBody AccountDetailsDTO accountDetailsDTO) {
        APIResponse<AccountDetailsDTO> apiResponse = new APIResponse<>();
        try {
            logger.info("Creating new account...");
            AccountDetailsDTO createdAccount = accountService.createAccount(accountDetailsDTO);
            logger.info("Account created successfully with ID: " + createdAccount.getAccountId());

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.CREATED);
            meta.setDisplayMessage("Account created successfully");
            apiResponse.setMeta(meta);
            apiResponse.setData(createdAccount);

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during account creation: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            meta.setDisplayMessage("Data integrity violation");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the account: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<APIResponse<AccountDetailsDTO>> getAccountById(@PathVariable Long accountId) {
        APIResponse<AccountDetailsDTO> apiResponse = new APIResponse<>();
        try {
            logger.info("Retrieving account with ID: " + accountId);
            AccountDetailsDTO accountDetailsDTO = accountService.getAccountById(accountId);
            logger.info("Account retrieved successfully for ID: " + accountId);

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.OK);
            meta.setDisplayMessage("Account retrieved successfully");
            apiResponse.setMeta(meta);
            apiResponse.setData(accountDetailsDTO);

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("Account not found");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the account: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<APIResponse<AccountDetailsDTO>> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AccountDetailsDTO accountDetailsDTO) {
        APIResponse<AccountDetailsDTO> apiResponse = new APIResponse<>();
        try {
            logger.info("Updating account with ID: " + accountId);
            AccountDetailsDTO updatedAccount = accountService.updateAccount(accountId, accountDetailsDTO);
            logger.info("Account updated successfully with ID: " + updatedAccount.getAccountId());

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.OK);
            meta.setDisplayMessage("Account updated successfully");
            apiResponse.setMeta(meta);
            apiResponse.setData(updatedAccount);

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("Account not found");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during account update: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            meta.setDisplayMessage("Data integrity violation");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the account: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<APIResponse<Void>> deleteAccount(@PathVariable Long accountId) {
        APIResponse<Void> apiResponse = new APIResponse<>();
        try {
            logger.info("Deleting account with ID: " + accountId);
            accountService.deleteAccount(accountId);
            logger.info("Account deleted successfully with ID: " + accountId);

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.NO_CONTENT);
            meta.setDisplayMessage("Account deleted successfully");
            apiResponse.setMeta(meta);

            return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("Account not found");
            apiResponse.setMeta(meta);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the account: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");
            apiResponse.setMeta(meta);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
