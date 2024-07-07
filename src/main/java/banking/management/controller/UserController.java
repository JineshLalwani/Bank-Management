package banking.management.controller;

import banking.management.dto.UserDetailsDTO;
import banking.management.model.User;
import banking.management.response.APIResponse;
import banking.management.response.ResponseMeta;
import banking.management.service.UserService;
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
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @PostMapping
    public ResponseEntity<APIResponse<UserDetailsDTO>> createUser(@Valid @RequestBody UserDetailsDTO userDetailsDTO) {
        try {
            logger.info("Creating new user...");
            UserDetailsDTO createdUser = userService.createUser(userDetailsDTO);
            logger.info("User created successfully with ID: " + createdUser.getUserId());
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.CREATED)
                    .displayMessage("User created successfully")
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(meta, createdUser));
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during user creation: " + e.getMessage());
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_ACCEPTABLE)
                    .displayMessage("Data integrity violation")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new APIResponse<>(meta, null));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the user: " + e.getMessage());
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(meta, null));
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<User>> getUserById(@PathVariable Long userId) {
        try {
            logger.info("Retrieving user with ID: " + userId);
            User user = userService.getUserById(userId);
            logger.info("User retrieved successfully for ID: " + userId);
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.OK)
                    .displayMessage("User retrieved successfully")
                    .build();
            return ResponseEntity.ok(new APIResponse<>(meta, user));
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "User not found with ID: " + userId, e);
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("User not found")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(meta, null));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the user: " + e.getMessage());
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(meta, null));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<APIResponse<UserDetailsDTO>> updateUser(@PathVariable Long userId, @RequestBody UserDetailsDTO userDetailsDTO) {
        try {
            logger.info("Updating user with ID: " + userId);
            UserDetailsDTO updatedUser = userService.updateUser(userId, userDetailsDTO);
            logger.info("User updated successfully for ID: " + userId);
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.OK)
                    .displayMessage("User updated successfully")
                    .build();
            return ResponseEntity.ok(new APIResponse<>(meta, updatedUser));
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "User not found with ID: " + userId);
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("User not found")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(meta, null));
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during user update: " + e.getMessage());
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_ACCEPTABLE)
                    .displayMessage("Data integrity violation")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new APIResponse<>(meta, null));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the user: " + e.getMessage());
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(meta, null));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable Long userId) {
        try {
            logger.info("Deleting user with ID: " + userId);
            userService.deleteUser(userId);
            logger.info("User deleted successfully for ID: " + userId);
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(true)
                    .statusCode(HttpStatus.NO_CONTENT)
                    .displayMessage("User deleted successfully")
                    .build();
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "User not found with ID: " + userId);
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .displayMessage("User not found")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(meta, null));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the user: " + e.getMessage());
            ResponseMeta meta = ResponseMeta.builder()
                    .isSuccess(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .displayMessage("Unexpected error occurred")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(meta, null));
        }
    }
}
