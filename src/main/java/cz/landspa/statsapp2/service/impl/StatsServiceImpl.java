package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.model.DTO.stats.GoalkeeperStats;
import cz.landspa.statsapp2.model.DTO.stats.PlayerStats;
import cz.landspa.statsapp2.model.DTO.stats.TeamStats;
import cz.landspa.statsapp2.model.DTO.stats.TeamStatsSum;
import cz.landspa.statsapp2.model.DTO.stats.game.*;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Team;
import cz.landspa.statsapp2.repository.GameRepository;
import cz.landspa.statsapp2.repository.GameStatsRepository;
import cz.landspa.statsapp2.repository.StatsRepository;
import cz.landspa.statsapp2.repository.TeamRepository;
import cz.landspa.statsapp2.service.StatsService;
import cz.landspa.statsapp2.util.PeriodTime;
import cz.landspa.statsapp2.util.Util;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {

    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    private final GameStatsRepository gameStatsRepository;
    private final StatsRepository statsRepository;

    public StatsServiceImpl(GameRepository gameRepository, TeamRepository teamRepository, GameStatsRepository gameStatsRepository, StatsRepository statsRepository) {
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
        this.gameStatsRepository = gameStatsRepository;
        this.statsRepository = statsRepository;
    }

    @Override
    public GamePlayerStatsSum getGamePlayerStats(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(()-> new IllegalArgumentException("Zápas nebyl nalezen"));

        List<PlayerGameStats> players = gameStatsRepository.getPlayerStats(gameId);
        List<GoalkeeperGameStats> goalkeepers = gameStatsRepository.getGoalkeeperStats(gameId);

        return new GamePlayerStatsSum(players, goalkeepers);
    }


    @Override
    public GameTeamStatsSum getGameTeamStats(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(()-> new IllegalArgumentException("Zápas nebyl nalezen"));

        List<PeriodStats> periodStats = new ArrayList<>();
        Map<Integer, PeriodTime> periodTimes = Util.getStartAndEndPeriodTimes(game);


        for (int i = 1; i <= periodTimes.size(); i++) {
            PeriodTime time = periodTimes.get(i);
            int startTime = time.startTime();
            int endTime = time.endTime() == null ? Integer.MAX_VALUE : time.endTime();

            PeriodStats period = statsRepository.getPeriodTeamStats(gameId,startTime,endTime, i);
            period.setPeriod(i);
            periodStats.add(period);
        }

        GameStats totalStats = statsRepository.getGameTeamStats(gameId);


        return new GameTeamStatsSum(periodStats, totalStats);
    }



    @Override
    public TeamStatsSum getTeamStats(Long teamId, String range) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Tým nebyl nalezen"));

        TeamStats teamStats;
        List<PlayerStats> playerStats;
        List<GoalkeeperStats> goalkeeperStats;

        Integer limit = null;
        Pageable pageable;
        LocalDate start = null;
        LocalDate end = null;

        switch (range) {
            case "last5" -> {
                limit = 5;
                pageable = Pageable.ofSize(limit);
            }
            default -> {
                pageable = Pageable.ofSize(Integer.MAX_VALUE);
                Map<String, LocalDate> dates = Util.getSeasonDates();
                start = dates.get("startDate");
                end = dates.get("endDate");
            }
        }

        teamStats = statsRepository.getTeamStats(teamId, start, end, pageable);
        playerStats = statsRepository.getPlayerStats(teamId, start, end, pageable);
        goalkeeperStats = statsRepository.getGoalkeeperStats(teamId, start, end, pageable);

        return new TeamStatsSum(teamStats, playerStats, goalkeeperStats);
    }

}
