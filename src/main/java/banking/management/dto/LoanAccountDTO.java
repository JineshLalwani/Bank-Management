package banking.management.dto;

import banking.management.model.LoanStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanAccountDTO {
    private Long accountId;
    private Long loanId;


    @Enumerated(EnumType.STRING)
    private String loanType;

    private Long loanAmount;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    private Long remainingAmount;

    private float monthlyEMI;
}
