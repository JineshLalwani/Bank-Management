package banking.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "banking_details")
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bankId;

    @Column(nullable = false)
    @NotEmpty(message = "Bank Name is required")
    private String bankName;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Bank IFSC is required")
    private String bankIfsc;

    @Column(nullable = false)
    @NotEmpty(message = "Bank Branch is required")
    private String bankBranch;

    @Column(nullable = false)
    @NotEmpty(message = "Bank Address is required")
    private String bankAddress;

    @Column(nullable = false)
    @NotEmpty(message = "Bank City is required")
    private String bankCity;

    @Column(nullable = false)
    @NotEmpty(message = "Bank District is required")
    private String bankDistrict;

    @Column(nullable = false)
    @NotEmpty(message = "Bank State is required")
    private String bankState;
}
