package banking.management.controller;

import banking.management.dto.UserDetailsDTO;
import banking.management.model.User;
import banking.management.response.APIResponse;
import banking.management.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDetailsDTO userDetailsDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setName("Jinesh Lalwani");
        user.setEmail("jinesh@gmail.com");
        user.setUserName("jinesh123");
        user.setAddress("456 Another St");
        user.setPhoneNumber("9876543210");
        user.setDob("1992-02-02");
        user.setPassword("securepassword");

        userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserId(1L);
        userDetailsDTO.setName("Jinesh Lalwani");
        userDetailsDTO.setEmail("jinesh@gmail.com");
        userDetailsDTO.setUserName("jinesh123");
        userDetailsDTO.setAddress("456 Another St");
        userDetailsDTO.setPhoneNumber("9876543210");
        userDetailsDTO.setDob("1992-02-02");
        userDetailsDTO.setPassword("securepassword");
    }

    @Test
    public void testCreateUser_Success() {
        when(userService.createUser(any(UserDetailsDTO.class))).thenReturn(userDetailsDTO);

        ResponseEntity<APIResponse<UserDetailsDTO>> response = userController.createUser(userDetailsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDetailsDTO, response.getBody().getData());
        assertTrue(response.getBody().getMeta().isSuccess());
    }


    @Test
    public void testCreateUser_Failure() {
        when(userService.createUser(any(UserDetailsDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<APIResponse<UserDetailsDTO>> response = userController.createUser(userDetailsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testGetUserById_Success() {
        when(userService.getUserById(anyLong())).thenReturn(user);

        ResponseEntity<APIResponse<User>> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody().getData());
        assertTrue(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userService.getUserById(anyLong())).thenThrow(new EntityNotFoundException("User not found with ID: 1"));

        ResponseEntity<APIResponse<User>> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testUpdateUser_Success() {
        userDetailsDTO.setName("Updated Jinesh Lalwani");
        when(userService.updateUser(anyLong(), any(UserDetailsDTO.class))).thenReturn(userDetailsDTO);

        ResponseEntity<APIResponse<UserDetailsDTO>> response = userController.updateUser(1L, userDetailsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDetailsDTO, response.getBody().getData());
        assertTrue(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testUpdateUser_NotFound() {
        when(userService.updateUser(anyLong(), any(UserDetailsDTO.class))).thenThrow(new EntityNotFoundException("User not found with ID: 1"));

        ResponseEntity<APIResponse<UserDetailsDTO>> response = userController.updateUser(1L, userDetailsDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().getMeta().isSuccess());
    }

    @Test
    public void testDeleteUser_Success() {
        doNothing().when(userService).deleteUser(anyLong());

        ResponseEntity<APIResponse<Void>> response = userController.deleteUser(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteUser_NotFound() {
        doThrow(new EntityNotFoundException("User not found with ID: 1")).when(userService).deleteUser(anyLong());

        ResponseEntity<APIResponse<Void>> response = userController.deleteUser(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().getMeta().isSuccess());
    }
}
