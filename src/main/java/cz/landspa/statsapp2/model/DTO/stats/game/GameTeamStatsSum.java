package cz.landspa.statsapp2.model.DTO.stats.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameTeamStatsSum {
    List<PeriodStats> periodStats;
    GameStats totalStats;
}
