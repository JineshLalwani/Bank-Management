package banking.management.service;

import banking.management.dto.UserDetailsDTO;
import banking.management.model.User;
import banking.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setName("Jinesh");
        userDetailsDTO.setEmail("jinesh@example.com");
        userDetailsDTO.setUserName("jinesh");
        userDetailsDTO.setAddress("India");
        userDetailsDTO.setPhoneNumber("9191919191");
        userDetailsDTO.setDob("1990-01-01");
        userDetailsDTO.setPassword("paswrde7");

        User user = new User();
        user.setUserId(1L);
        user.setName("Jinesh");
        user.setEmail("jinesh.doe@example.com");
        user.setUserName("jinesh");
        user.setAddress("India");
        user.setPhoneNumber("9191919191");
        user.setDob("1990-01-01");
        user.setPassword("paswrde7");


        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDetailsDTO createdUser = userService.createUser(userDetailsDTO);

        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getUserId());
        assertEquals(userDetailsDTO.getName(), createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Jinesh");
        user.setEmail("jinesh.doe@example.com");
        user.setUserName("jinesh");
        user.setAddress("India");
        user.setPhoneNumber("9191919191");
        user.setDob("1990-01-01");
        user.setPassword("paswrde7");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User userDetailsDTO = userService.getUserById(1L);

        assertNotNull(userDetailsDTO);
        assertEquals(user.getUserId(), userDetailsDTO.getUserId());
        assertEquals(user.getName(), userDetailsDTO.getName());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateUser() {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setName("Jinesh Updated");
        userDetailsDTO.setEmail("jinesh.updated@example.com");
        userDetailsDTO.setUserName("jineshupdated");
        userDetailsDTO.setAddress("India");
        userDetailsDTO.setPhoneNumber("9765432193");
        userDetailsDTO.setDob("1991-01-01");
        userDetailsDTO.setPassword("newpassword");

        User user = new User();
        user.setUserId(1L);
        user.setName("Jinesh");
        user.setEmail("jinesh.doe@example.com");
        user.setUserName("jinesh");
        user.setAddress("India");
        user.setPhoneNumber("9191919191");
        user.setDob("1990-01-01");
        user.setPassword("paswrde7");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDetailsDTO updatedUser = userService.updateUser(1L, userDetailsDTO);

        assertNotNull(updatedUser);
        assertEquals(1L, updatedUser.getUserId());
        assertEquals("Jinesh Updated", updatedUser.getName());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(1L, userDetailsDTO);
        });

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(0)).deleteById(anyLong());
    }
}
