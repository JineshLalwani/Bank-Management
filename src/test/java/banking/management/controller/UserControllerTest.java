package banking.management.controller;

import banking.management.dto.UserDetailsDTO;
import banking.management.model.User;
import banking.management.response.APIResponse;
import banking.management.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private UserDetailsDTO userDetailsDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserId(1L);
        userDetailsDTO.setName("John Doe");
        userDetailsDTO.setEmail("john.doe@example.com");
        userDetailsDTO.setUserName("johndoe");
        userDetailsDTO.setAddress("123 Main St");
        userDetailsDTO.setPhoneNumber("1234567890");
        userDetailsDTO.setDob("1990-01-01");
        userDetailsDTO.setPassword("password");
    }

    @Test
    public void testCreateUser() throws Exception {
        given(userService.createUser(any(UserDetailsDTO.class))).willReturn(userDetailsDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"userName\":\"johndoe\",\"address\":\"123 Main St\",\"phoneNumber\":\"1234567890\",\"dob\":\"1990-01-01\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        given(userService.getUserById(anyLong())).willReturn(user);

        mockMvc.perform(get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        given(userService.getUserById(anyLong())).willThrow(new EntityNotFoundException("User not found with ID: 1"));

        mockMvc.perform(get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUser() throws Exception {
        given(userService.updateUser(anyLong(), any(UserDetailsDTO.class))).willReturn(userDetailsDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"userName\":\"johndoe\",\"address\":\"123 Main St\",\"phoneNumber\":\"1234567890\",\"dob\":\"1990-01-01\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        given(userService.updateUser(anyLong(), any(UserDetailsDTO.class))).willThrow(new EntityNotFoundException("User not found with ID: 1"));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"userName\":\"johndoe\",\"address\":\"123 Main St\",\"phoneNumber\":\"1234567890\",\"dob\":\"1990-01-01\",\"password\":\"password\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        doThrow(new EntityNotFoundException("User not found with ID: 1")).when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
