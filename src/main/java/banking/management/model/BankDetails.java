package banking.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "bank_master")
public class BankDetails implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ifsc",unique=true)
    private String ifsc;

    @Column(name = "branch")
    private String branch;

    @Column(name = "micr")
    private String micr;

    @Column(name = "contact")
    private String contact;

    @Column(name = "upi")
    private boolean upi;

    @Column(name = "rtgs")
    private boolean rtgs;

    @Column(name = "neft")
    private boolean neft;

    @Column(name = "imps")
    private boolean imps;

    @Column(name = "swift")
    private String swift;

    @Column(name = "iso_3166")
    private String iso3166;

    @Column(name = "bank")
    private String bank;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "city")
    private String city;

    @Column(name = "centre")
    private String centre;

    @Column(name = "state")
    private String state;

    @Column(name = "district")
    private String district;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}
