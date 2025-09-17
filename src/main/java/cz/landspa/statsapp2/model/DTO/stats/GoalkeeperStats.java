package cz.landspa.statsapp2.model.DTO.stats;

import cz.landspa.statsapp2.model.entity.Player;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class GoalkeeperStats {
    private Player player;

    private Long gamesPlayed;
    private Long goalsConceded;
    private Long saves;

    private Float avgGoals;
    private Float savePercentage;

    public GoalkeeperStats(Player player, Long gamesPlayed, Long goalsConceded, Long saves) {
        this.player = player;
        this.gamesPlayed = defaultIfNull(gamesPlayed);
        this.goalsConceded = defaultIfNull(goalsConceded);
        this.saves = defaultIfNull(saves);

        this.avgGoals = this.gamesPlayed > 0 ? roundTo2((float) this.goalsConceded / this.gamesPlayed) : null;
        this.savePercentage = (this.saves + this.goalsConceded) > 0
                ? roundTo2Percent((float) this.saves / (this.saves + this.goalsConceded))
                : null;
    }

    private Long defaultIfNull(Long value) {
        return value == null ? 0L : value;
    }

    private Float roundTo2Percent(float value) {
        return Math.round(value * 10000f) / 100f;
    }
    private Float roundTo2(float value) {
        return Math.round(value * 100f) / 100f;
    }

}
