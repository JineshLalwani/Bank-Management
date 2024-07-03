package banking.management.dto;

import banking.management.helper.ValidationHelper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private Long userId;

    @NotEmpty(message = "Name is required")
    private String name;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Please enter your username")
    private String userName;

    @NotEmpty(message = "Address is required")
    private String address;

    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;

    @NotEmpty(message = "Date of birth is required")
    private String dob;

    @NotEmpty(message = "Password is required")
    @Size(min = 10)
    private String password;

    public void validate() {
        if (!ValidationHelper.isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number. It must be exactly 10 digits.");
        }
        if (!ValidationHelper.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        if (!ValidationHelper.isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password. It must be at least 8 characters long.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsDTO that = (UserDetailsDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(userName, that.userName) && Objects.equals(address, that.address) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(dob, that.dob) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, email, userName, address, phoneNumber, dob, password);
    }
}
