package cz.landspa.statsapp2.model.entity.event;

import cz.landspa.statsapp2.model.entity.Roster;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DiscriminatorValue("GoalConceded")
public class GoalConceded extends Event{

    @ManyToMany
    @JoinTable(
            name = "goal_conceded_on_ice",
            joinColumns = @JoinColumn(name = "goal_conceded_id"),
            inverseJoinColumns = @JoinColumn(name = "roster_id")
    )
    List<Roster> onIce;

    @ManyToOne
    Roster inGoal;

    @NotNull(message = "Situace musí být vyplněna")
    String situation;

    boolean majorPenalty;
}
