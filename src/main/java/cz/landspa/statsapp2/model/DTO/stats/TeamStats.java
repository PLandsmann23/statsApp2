package cz.landspa.statsapp2.model.DTO.stats;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamStats {
    private Float ppPercentage;
    private Float shPercentage;
    private Float shotEfficiency;
    private Float saveEfficiency;
    private Float avgScored;
    private Float avgConceded;

    public TeamStats(Long ppGoals, Long ppMajorGoals, Long ppNumber,
                     Long shGoals, Long shMajorGoals, Long shNumber,
                     Long totalShots, Long totalSaves,
                     Long games, Long scoredGoals,
                     Long concededGoals, Long concededGoalsNoEmpty) {

        ppGoals = defaultIfNull(ppGoals);
        ppMajorGoals = defaultIfNull(ppMajorGoals);
        ppNumber = defaultIfNull(ppNumber);
        shGoals = defaultIfNull(shGoals);
        shMajorGoals = defaultIfNull(shMajorGoals);
        shNumber = defaultIfNull(shNumber);
        totalShots = defaultIfNull(totalShots);
        totalSaves = defaultIfNull(totalSaves);
        games = defaultIfNull(games);
        scoredGoals = defaultIfNull(scoredGoals);
        concededGoals = defaultIfNull(concededGoals);
        concededGoalsNoEmpty = defaultIfNull(concededGoalsNoEmpty);

        long totalPP = ppMajorGoals + ppNumber;
        long totalSH = shMajorGoals + shNumber;
        long totalShotsFaced = totalSaves + concededGoalsNoEmpty;

        this.ppPercentage = totalPP > 0 ? roundTo2Percent((float) ppGoals / totalPP) : null;
        this.shPercentage = totalSH > 0 ? roundTo2Percent((float) (shNumber - shGoals) / totalSH) : null;
        this.shotEfficiency = totalShots > 0 ? roundTo2Percent((float) scoredGoals / totalShots) : null;
        this.saveEfficiency = totalShotsFaced > 0 ? roundTo2Percent((float) totalSaves / totalShotsFaced) : null;
        this.avgScored = games > 0 ? roundTo2((float) scoredGoals / games) : null;
        this.avgConceded = games > 0 ? roundTo2((float) concededGoals / games) : null;
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
