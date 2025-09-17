package cz.landspa.statsapp2.model.entity.event;


import com.fasterxml.jackson.annotation.JsonProperty;
import cz.landspa.statsapp2.model.entity.Roster;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DiscriminatorValue("GoalScored")
public class GoalScored extends Event{

        @ManyToOne
        private Roster scorer;

        @ManyToMany
        @JoinTable(
                name = "goal_assists",
                joinColumns = @JoinColumn(name = "goal_id"),
                inverseJoinColumns = @JoinColumn(name = "roster_id")
        )
        private List<Roster> assists;


        @ManyToMany
        @JoinTable(
                name = "goal_on_ice",
                joinColumns = @JoinColumn(name = "goal_id"),
                inverseJoinColumns = @JoinColumn(name = "roster_id")
        )
        private List<Roster> onIce;

        @NotNull(message = "Situace musí být vyplněna")
        private String situation;

        private boolean majorPenalty;


        @JsonProperty("onIce")
        public List<Roster> getOnIce(GoalScored goal) {
                List<Roster> all = goal.getOnIce();

                List<Roster> result = new ArrayList<>();

                Long scorerId = goal.getScorer() != null ? goal.getScorer().getId() : null;
                List<Long> assistIds = goal.getAssists().stream()
                        .map(a -> a != null ? a.getId() : null)
                        .toList();

                // Přidej střelce, pokud existuje
                if (scorerId != null) {
                        all.stream()
                                .filter(r -> r.getId().equals(scorerId))
                                .findFirst()
                                .ifPresent(result::add);
                }

                // Přidej asistenty, pokud existují
                for (Long assistId : assistIds) {
                        if (assistId != null) {
                                all.stream()
                                        .filter(r -> r.getId().equals(assistId))
                                        .findFirst()
                                        .ifPresent(r -> {
                                                if (!result.contains(r)) result.add(r);
                                        });
                        }
                }

                // Přidej zbytek, který tam ještě není
                for (Roster r : all) {
                        if (!result.contains(r)) {
                                result.add(r);
                        }
                }

                return result;
        }


}
