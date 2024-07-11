package banking.management.controller;

import banking.management.dto.UserDetailsDTO;
import banking.management.model.User;
import banking.management.response.APIResponse;
import banking.management.response.ResponseMeta;
//import banking.management.service.OCRServiceImpl;
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

//    @Autowired
//    private OCRServiceImpl ocrService;
    private ResponseMeta meta=new ResponseMeta();

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @PostMapping
    public ResponseEntity<APIResponse<UserDetailsDTO>> createUser(@Valid @RequestBody UserDetailsDTO userDetailsDTO) {
        APIResponse<UserDetailsDTO>apiResponse = new APIResponse<>();

        try {
            logger.info("Creating new user...");
            UserDetailsDTO createdUser = userService.createUser(userDetailsDTO);
            logger.info("User created successfully with ID: " + createdUser.getUserId());

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.CREATED);
            meta.setDisplayMessage("User created successfully");
            apiResponse.setMeta(meta);
            apiResponse.setData(createdUser);

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during user creation: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            meta.setDisplayMessage("Data integrity violation");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the user: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<User>> getUserById(@PathVariable Long userId) {
        APIResponse<User>apiResponse = new APIResponse<>();

        try {
            logger.info("Retrieving user with ID: " + userId);
            User user = userService.getUserById(userId);
            logger.info("User retrieved successfully for ID: " + userId);

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.OK);
            meta.setDisplayMessage("User retrieved successfully");
            apiResponse.setMeta(meta);
            apiResponse.setData(user);

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "User not found with ID: " + userId, e);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("User not found");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the user: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<APIResponse<UserDetailsDTO>> updateUser(@PathVariable Long userId, @RequestBody UserDetailsDTO userDetailsDTO) {
        APIResponse<UserDetailsDTO>apiResponse = new APIResponse<>();

        try {
            logger.info("Updating user with ID: " + userId);
            UserDetailsDTO updatedUser = userService.updateUser(userId, userDetailsDTO);
            logger.info("User updated successfully for ID: " + userId);

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.OK);
            meta.setDisplayMessage("User updated successfully");
            apiResponse.setMeta(meta);
            apiResponse.setData(updatedUser);

            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "User not found with ID: " + userId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("User not found");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, "Data integrity violation during user update: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            meta.setDisplayMessage("Data integrity violation");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the user: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable Long userId) {
        APIResponse<Void>apiResponse = new APIResponse<>();

        try {
            logger.info("Deleting user with ID: " + userId);
            userService.deleteUser(userId);
            logger.info("User deleted successfully for ID: " + userId);

            meta.setSuccess(true);
            meta.setStatusCode(HttpStatus.NO_CONTENT);
            meta.setDisplayMessage("User deleted successfully");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "User not found with ID: " + userId);

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.NOT_FOUND);
            meta.setDisplayMessage("User not found");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the user: " + e.getMessage());

            meta.setSuccess(false);
            meta.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            meta.setDisplayMessage("Unexpected error occurred");
            apiResponse.setMeta(meta);
            apiResponse.setData(null);

            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/unirest")
//    public String unirestUser() {
//        return ocrService.getOCR();
//    }
}
