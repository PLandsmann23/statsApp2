package cz.landspa.statsapp2.model.DTO.stats.game;

import cz.landspa.statsapp2.model.entity.Roster;
import cz.landspa.statsapp2.model.entity.stats.SavesRecord;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class GoalkeeperGameStats {
    private Roster player;
    private Long saves = 0L;
    private List<SavesRecord> periodSaves = new ArrayList<>();
    private Long goals = 0L;
    private Float percentage;

    public GoalkeeperGameStats(Roster player, List<SavesRecord> periodSaves, Long goals) {
        this.player = player;
        this.periodSaves = periodSaves;
        for(SavesRecord s: periodSaves){
            this.saves += s.getSaves();
        }
        this.goals = goals;
        if (this.saves + this.goals != 0) {
            this.percentage = ((float) this.saves / (this.saves + this.goals)) * 100;
        } else {
            this.percentage = null;
        }
    }

    public Long getSavesForPeriod(int period) {
        return periodSaves.stream()
                .filter(s -> s.getPeriod() == period)
                .map(SavesRecord::getSaves)
                .findFirst()
                .orElse(null);
    }
}
