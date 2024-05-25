package ir.farzadafi.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private LocationHierarchy province;

    @OneToOne
    private LocationHierarchy county;

    @OneToOne
    private LocationHierarchy city;

    private String street;
}
