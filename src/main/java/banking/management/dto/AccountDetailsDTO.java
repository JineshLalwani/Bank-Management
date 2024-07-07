package banking.management.dto;

import banking.management.model.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsDTO {

    private Long accountId;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private int currentBalance;


    private String accountStatus;

    private float monthlyInterest;

    private List<Long> userId;

    private String bankIfsc;
}
