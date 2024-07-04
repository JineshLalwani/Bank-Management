package banking.management.controller;

import banking.management.dto.AccountDetailsDTO;
import banking.management.response.APIResponse;
import banking.management.response.ResponseMeta;
import banking.management.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
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

    private static final Logger logger = Logger.getLogger(AccountController.class.getName());

    @PostMapping
    public ResponseEntity<APIResponse<AccountDetailsDTO>> createAccount(@RequestBody AccountDetailsDTO accountDetailsDTO) {
        try {
            logger.info("Creating new account...");
            AccountDetailsDTO createdAccount = accountService.createAccount(accountDetailsDTO);
            logger.info("Account created successfully with ID: " + createdAccount.getAccountId());
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(true, HttpStatus.CREATED, "Account created successfully"), createdAccount), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during account creation: " + e.getMessage());
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.NOT_ACCEPTABLE, "Data integrity violation"), null), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the account: " + e.getMessage());
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<APIResponse<AccountDetailsDTO>> getAccountById(@PathVariable Long accountId) {
        try {
            logger.info("Retrieving account with ID: " + accountId);
            AccountDetailsDTO accountDetailsDTO = accountService.getAccountById(accountId);
            logger.info("Account retrieved successfully for ID: " + accountId);
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(true, HttpStatus.OK, "Account retrieved successfully"), accountDetailsDTO), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.NOT_FOUND, "Account not found"), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the account: " + e.getMessage());
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<APIResponse<AccountDetailsDTO>> updateAccount(@PathVariable Long accountId, @RequestBody AccountDetailsDTO accountDetailsDTO) {
        try {
            logger.info("Updating account with ID: " + accountId);
            AccountDetailsDTO updatedAccount = accountService.updateAccount(accountId, accountDetailsDTO);
            logger.info("Account updated successfully with ID: " + updatedAccount.getAccountId());
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(true, HttpStatus.OK, "Account updated successfully"), updatedAccount), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.NOT_FOUND, "Account not found"), null), HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during account update: " + e.getMessage());
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.NOT_ACCEPTABLE, "Data integrity violation"), null), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the account: " + e.getMessage());
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<APIResponse<Void>> deleteAccount(@PathVariable Long accountId) {
        try {
            logger.info("Deleting account with ID: " + accountId);
            accountService.deleteAccount(accountId);
            logger.info("Account deleted successfully with ID: " + accountId);
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(true, HttpStatus.NO_CONTENT, "Account deleted successfully"), null), HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.NOT_FOUND, "Account not found"), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the account: " + e.getMessage());
            return new ResponseEntity<>(new APIResponse<>(new ResponseMeta(false, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
