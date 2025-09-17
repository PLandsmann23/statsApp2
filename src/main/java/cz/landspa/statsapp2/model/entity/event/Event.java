package cz.landspa.statsapp2.model.entity.event;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.landspa.statsapp2.model.entity.Game;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = GoalScored.class, name = "GoalScored"),
        @JsonSubTypes.Type(value = GoalConceded.class, name = "GoalConceded"),
        @JsonSubTypes.Type(value = Penalty.class, name = "Penalty"),
        @JsonSubTypes.Type(value = OpponentPenalty.class, name = "OpponentPenalty")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Délka trestu musí být vyplněna")
    private Integer time;

    @ManyToOne
    @JsonIgnore
    private Game game;

    public String getType(){
        return this.getClass().getSimpleName();
    }
}
