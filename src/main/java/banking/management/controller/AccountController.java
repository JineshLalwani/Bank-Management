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

    private static final Logger logger = Logger.getLogger(AccountController.class.getName());

    @PostMapping
    public ResponseEntity<APIResponse<AccountDetailsDTO>> createAccount(@Valid @RequestBody AccountDetailsDTO accountDetailsDTO) {
        try {
            logger.info("Creating new account...");
            AccountDetailsDTO createdAccount = accountService.createAccount(accountDetailsDTO);
            logger.info("Account created successfully with ID: " + createdAccount.getAccountId());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.CREATED)
                    .displayMessage("Account created successfully")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(createdAccount)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during account creation: " + e.getMessage());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_ACCEPTABLE)
                    .displayMessage("Data integrity violation")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the account: " + e.getMessage());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<APIResponse<AccountDetailsDTO>> getAccountById(@PathVariable Long accountId) {
        try {
            logger.info("Retrieving account with ID: " + accountId);
            AccountDetailsDTO accountDetailsDTO = accountService.getAccountById(accountId);
            logger.info("Account retrieved successfully for ID: " + accountId);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.OK)
                    .displayMessage("Account retrieved successfully")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(accountDetailsDTO)
                    .build();

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Account not found")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the account: " + e.getMessage());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<APIResponse<AccountDetailsDTO>> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AccountDetailsDTO accountDetailsDTO) {
        try {
            logger.info("Updating account with ID: " + accountId);
            AccountDetailsDTO updatedAccount = accountService.updateAccount(accountId, accountDetailsDTO);
            logger.info("Account updated successfully with ID: " + updatedAccount.getAccountId());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.OK)
                    .displayMessage("Account updated successfully")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(updatedAccount)
                    .build();

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Account not found")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during account update: " + e.getMessage());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_ACCEPTABLE)
                    .displayMessage("Data integrity violation")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the account: " + e.getMessage());

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();

            APIResponse<AccountDetailsDTO> apiResponse = APIResponse.<AccountDetailsDTO>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<APIResponse<Void>> deleteAccount(@PathVariable Long accountId) {
        try {
            logger.info("Deleting account with ID: " + accountId);
            accountService.deleteAccount(accountId);
            logger.info("Account deleted successfully with ID: " + accountId);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.NO_CONTENT)
                    .displayMessage("Account deleted successfully")
                    .build();

            APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "Account not found with ID: " + accountId);

            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("Account not found")
                    .build();

            APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                    .meta(meta)
                    .data(null)
                    .build();

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the account: " + e.getMessage());

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
