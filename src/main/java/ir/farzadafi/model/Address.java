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

    @ManyToOne
    private LocationHierarchy province;

    @ManyToOne
    private LocationHierarchy county;

    @ManyToOne
    private LocationHierarchy city;

    private String street;
}
