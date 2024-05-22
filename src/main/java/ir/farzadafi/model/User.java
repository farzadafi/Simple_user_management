package ir.farzadafi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

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

    private Date birthdate;

    @Column(name = "created_in")
    @CreationTimestamp
    private LocalDateTime createdIn;
}
