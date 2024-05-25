package ir.farzadafi.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class LocationHierarchy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private LocationHierarchy locationHierarchy;

    public LocationHierarchy(Integer id) {
        this.id = id;
    }

    public void setLocationHierarchyById(int id) {
        this.locationHierarchy = new LocationHierarchy(id);
    }
}
