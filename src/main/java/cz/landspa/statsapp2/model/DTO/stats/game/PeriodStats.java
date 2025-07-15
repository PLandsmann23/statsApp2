package cz.landspa.statsapp2.model.DTO.stats.game;


import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PeriodStats extends GameStats{
    Integer period;

    public PeriodStats(Long teamGoals, Long opponentGoals, Long teamSaves, Long opponentSaves, Long teamShots, Long opponentShots, Long teamPowerPlays, Long opponentPowerPlays, Long teamPPGoals, Long opponentPPGoals, Long teamSHGoals, Long opponentSHGoals, Long teamPPMajorGoals, Long opponentPPMajorGoals) {
        super(teamGoals, opponentGoals, teamSaves, opponentSaves, teamShots, opponentShots, teamPowerPlays, opponentPowerPlays, teamPPGoals, opponentPPGoals, teamSHGoals, opponentSHGoals, teamPPMajorGoals, opponentPPMajorGoals);
    }
}
