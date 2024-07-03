package banking.management.service;

import banking.management.dto.UserDetailsDTO;
import banking.management.model.User;

public interface UserService {
    UserDetailsDTO createUser(UserDetailsDTO userDetailsDTO);
    User getUserById(Long userId);
    UserDetailsDTO updateUser(Long userId, UserDetailsDTO userDetailsDTO);
    void deleteUser(Long userId);
}
