package banking.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "banking_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne
    @JoinColumn()
    private Details details;

    @ManyToMany
    @JsonIgnore
    private List<User> users;

    @OneToMany
    @JoinColumn
    private List<Payment> payments;

    @OneToMany
    @JoinColumn()
    private List<Loan> loans;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private int currentBalance;

    @Column(nullable = false)
    private LocalDate dateOpened;

    private String accountStatus;

    @Column
    private float monthlyInterest;
}
