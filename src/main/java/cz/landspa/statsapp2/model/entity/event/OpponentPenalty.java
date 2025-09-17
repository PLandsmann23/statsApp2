package cz.landspa.statsapp2.model.entity.event;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
@DiscriminatorValue("OpponentPenalty")
public class OpponentPenalty extends Event{

    @NotNull(message = "Délka trestu musí být vyplněna")
    private Integer minutes;

    private Boolean coincidental = false;

}
