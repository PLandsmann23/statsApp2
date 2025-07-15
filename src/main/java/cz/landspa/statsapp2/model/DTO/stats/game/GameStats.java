package cz.landspa.statsapp2.model.DTO.stats.game;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameStats{

    Long teamGoals;
    Long opponentGoals;
    Long teamSaves;
    Long opponentSaves;
    Long teamShots;
    Long opponentShots;
    Long teamPIM;
    Long opponentPIM;
    Long teamPPGoals;
    Long opponentPPGoals;
    Long teamSHGoals;
    Long opponentSHGoals;

    Float teamShotEfficiency;
    Float opponentShotEfficiency;

    Float teamPPEfficiency;
    Float opponentPPEfficiency;

    Float teamSHEfficiency;
    Float opponentSHEfficiency;


    public GameStats(Long teamGoals, Long opponentGoals,
                     Long teamSaves, Long opponentSaves,
                     Long teamShots, Long opponentShots,
                     Long teamPowerPlays, Long opponentPowerPlays,
                     Long teamPPGoals, Long opponentPPGoals,
                     Long teamSHGoals, Long opponentSHGoals,
                     Long teamPPMajorGoals, Long opponentPPMajorGoals) {

        this.teamGoals = defaultIfNull(teamGoals);
        this.opponentGoals = defaultIfNull(opponentGoals);
        this.teamSaves = defaultIfNull(teamSaves);
        this.opponentSaves = defaultIfNull(opponentSaves);
        this.teamShots = defaultIfNull(teamShots);
        this.opponentShots = defaultIfNull(opponentShots);
        this.teamPIM = defaultIfNull(teamPIM);
        this.opponentPIM = defaultIfNull(opponentPIM);
        this.teamPPGoals = defaultIfNull(teamPPGoals);
        this.opponentPPGoals = defaultIfNull(opponentPPGoals);
        this.teamSHGoals = defaultIfNull(teamSHGoals);
        this.opponentSHGoals = defaultIfNull(opponentSHGoals);

        long teamPPOpps = teamPowerPlays + teamPPMajorGoals;
        long opponentPPOpps = opponentPowerPlays + opponentPPMajorGoals;


        this.teamShotEfficiency = teamShots > 0 ? roundTo2((float) teamGoals / teamShots) : null;
        this.opponentShotEfficiency = opponentShots > 0 ? roundTo2((float) opponentGoals / opponentShots) : null;

        this.teamPPEfficiency = teamPPOpps > 0 ? roundTo2((float) teamPPGoals / teamPPOpps) : null;
        this.opponentPPEfficiency = opponentPPOpps > 0 ? roundTo2((float) opponentPPGoals / opponentPPOpps) : null;

        this.teamSHEfficiency = this.opponentPPEfficiency != null ? 100- this.opponentPPEfficiency : null;
        this.opponentSHEfficiency = this.teamPPEfficiency != null ? 100-this.teamPPEfficiency : null;
    }

    private Long defaultIfNull(Long value) {
        return value == null ? 0L : value;
    }

    private Float roundTo2(float value) {
        return Math.round(value * 10000f) / 100f;
    }


}
