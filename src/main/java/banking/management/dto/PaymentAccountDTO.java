package banking.management.dto;

import banking.management.model.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAccountDTO {
    private Long accountId;

    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(nullable = false)
    private int amountWithdrawn;

    @Column(nullable = false)
    private int amountDeposited;


}
