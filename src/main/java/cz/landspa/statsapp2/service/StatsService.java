package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.DTO.stats.TeamStatsSum;
import cz.landspa.statsapp2.model.DTO.stats.game.GamePlayerStatsSum;
import cz.landspa.statsapp2.model.DTO.stats.game.GameTeamStatsSum;
import org.springframework.stereotype.Service;

@Service
public interface StatsService {

    GamePlayerStatsSum getGamePlayerStats(Long gameId);

    GameTeamStatsSum getGameTeamStats(Long gameId);

    TeamStatsSum getTeamStats (Long gameId, String range);
}
