package banking.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "banking_loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Account account;


    private String loanType;

    @Column(nullable = false)
    private Long loanAmount;

    @Column(nullable = false)
    private LocalDate dateOpened;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus loanStatus;

    @Column(nullable = false)
    private Long remainingAmount;

    @Column
    private float monthlyEMI;
}
