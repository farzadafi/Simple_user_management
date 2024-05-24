package ir.farzadafi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VerificationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    private int code;

    @CreationTimestamp
    private LocalDateTime created_in;

    public VerificationUser(User user, int code) {
        this.user = user;
        this.code = code;
    }

    @Transient
    public int calculatePastVerificationTime() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(this.getCreated_in(), now);
        return (int) duration.toMinutes();
    }
}
