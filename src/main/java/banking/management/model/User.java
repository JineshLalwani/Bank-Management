package banking.management.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "banking_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotEmpty(message = "Name is required")
    private String name;

    @ManyToMany
    @JoinTable()
    private List<Account> accounts;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotEmpty(message="Please enter your username")
    private String userName;

    @NotEmpty(message = "Address is required")
    private String address;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "DOB", nullable = false)
    private String dob;

    @Column(nullable = false)
    private String password;
}
