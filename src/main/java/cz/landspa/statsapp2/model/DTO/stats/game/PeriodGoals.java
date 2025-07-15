package cz.landspa.statsapp2.model.DTO.stats.game;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PeriodGoals {
    Integer period;

    Long goalsScored;

    Long goalsConceded;
}
