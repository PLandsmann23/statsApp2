package cz.landspa.statsapp2.model.entity.event;


import cz.landspa.statsapp2.model.entity.Roster;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("Penalty")
public class Penalty extends Event{

    @ManyToOne
    private Roster player;

    @NotNull(message = "Délka trestu musí být vyplněna")
    private Integer minutes;

    private String reason;

    private Boolean coincidental = false;
}
