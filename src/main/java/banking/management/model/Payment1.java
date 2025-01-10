package banking.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Audited
@EntityListeners(AuditingEntityListener.class)
@Table(name = "banking_payments1")
@EqualsAndHashCode(callSuper = false)
public class Payment1 extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Account account;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(nullable = false)
    private int amountWithdrawn;

    @Column(nullable = false)
    private int amountDeposited;

    @Column(nullable = false)
    private int accountBalance;

    @Column(nullable = false)
    private LocalDate transactionDate;
}
