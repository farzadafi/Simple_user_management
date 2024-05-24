package ir.farzadafi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_national_code", columnNames = "national_code"),
        @UniqueConstraint(name = "UK_email", columnNames = "email")
})
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id = ?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;

    private String lastname;

    @Column(unique = true, name = "national_code")
    private String nationalCode;

    @Column(unique = true)
    private String email;

    private String password;

    private LocalDate birthdate;

    @Column(name = "created_in")
    @CreationTimestamp
    private LocalDateTime createdIn;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private VerificationUser verificationUser;

    private boolean enabled = Boolean.FALSE;

    private boolean deleted = Boolean.FALSE;

    @PrePersist
    public void setUserVerification() {
        Random random = new Random();
        int code = random.nextInt(99999);
        VerificationUser verificationUser = new VerificationUser(this, code);
        this.setVerificationUser(verificationUser);
    }
}
