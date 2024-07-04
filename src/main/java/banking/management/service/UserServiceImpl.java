package banking.management.service;

import banking.management.dto.UserDetailsDTO;
import banking.management.model.User;
import banking.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    @Transactional
    public UserDetailsDTO createUser(UserDetailsDTO userDetailsDTO) {
        try {
            logger.info("Creating a new user...");
            userDetailsDTO.validate();

            User user = new User();
            user.setName(userDetailsDTO.getName());
            user.setEmail(userDetailsDTO.getEmail());
            user.setUserName(userDetailsDTO.getUserName());
            user.setAddress(userDetailsDTO.getAddress());
            user.setPhoneNumber(userDetailsDTO.getPhoneNumber());
            user.setDob(userDetailsDTO.getDob());
            user.setPassword(userDetailsDTO.getPassword());

            User savedUser = userRepository.save(user);
            logger.info("User created successfully with ID: " + savedUser.getUserId());

            return mapToDTO(savedUser);
        }catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the user: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while creating the user");
        }
    }

    @Override
    public User getUserById(Long userId) {
        try {
            logger.info("Retrieving user with ID: " + userId);
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new EntityNotFoundException("User not found with ID: " + userId);
            }
            logger.info("User retrieved successfully for ID: " + userId);
            return userOptional.get();
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the user: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while retrieving the user");
        }
    }

    @Override
    @Transactional
    public UserDetailsDTO updateUser(Long userId, UserDetailsDTO userDetailsDTO) {
        try {
            logger.info("Updating user with ID: " + userId);
            Optional<User> userOptional = userRepository.findById(userId);
            if (!userOptional.isPresent()) {
                throw new EntityNotFoundException("User not found with ID: " + userId);
            }
            userDetailsDTO.validate();



            User user = userOptional.get();
            user.setName(userDetailsDTO.getName());
            user.setEmail(userDetailsDTO.getEmail());
            user.setUserName(userDetailsDTO.getUserName());
            user.setAddress(userDetailsDTO.getAddress());
            user.setPhoneNumber(userDetailsDTO.getPhoneNumber());
            user.setDob(userDetailsDTO.getDob());
            user.setPassword(userDetailsDTO.getPassword());

            User updatedUser = userRepository.save(user);
            logger.info("User updated successfully with ID: " + updatedUser.getUserId());

            return mapToDTO(updatedUser);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the user: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while updating the user");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        try {
            logger.info("Deleting user with ID: " + userId);
            if (!userRepository.existsById(userId)) {
                throw new EntityNotFoundException("User not found with ID: " + userId);
            }
            userRepository.deleteById(userId);
            logger.info("User deleted successfully with ID: " + userId);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the user: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while deleting the user");
        }
    }

    private UserDetailsDTO mapToDTO(User user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUserName());
        dto.setAddress(user.getAddress());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDob(user.getDob());
        dto.setPassword(user.getPassword());
        return dto;
    }
}
