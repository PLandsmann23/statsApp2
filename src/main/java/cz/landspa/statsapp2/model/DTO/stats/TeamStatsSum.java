package cz.landspa.statsapp2.model.DTO.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamStatsSum {

    TeamStats teamStats;

    List<PlayerStats> playerStats;

    List<GoalkeeperStats> goalkeeperStats;
}
