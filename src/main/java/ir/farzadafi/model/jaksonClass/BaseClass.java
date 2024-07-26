package ir.farzadafi.model.jaksonClass;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = BaseClass.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleSubClass.class, names = "SimpleSubClass"),
        @JsonSubTypes.Type(value = SubClassA.class, names = "SubClassA"),
        @JsonSubTypes.Type(value = SubClassB.class, names = "SubClassB")
})
public abstract class BaseClass {
    private String name;
}
