package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.exception.MissingActiveGoalkeeperException;
import cz.landspa.statsapp2.model.DTO.event.PeriodEvents;
import cz.landspa.statsapp2.model.DTO.game.GameFullInfoDTO;
import cz.landspa.statsapp2.model.DTO.stats.game.PeriodGoals;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Roster;
import cz.landspa.statsapp2.model.entity.event.Event;
import cz.landspa.statsapp2.model.entity.stats.SavesRecord;
import cz.landspa.statsapp2.model.entity.stats.ShotsRecord;
import cz.landspa.statsapp2.repository.*;
import cz.landspa.statsapp2.service.GameService;
import cz.landspa.statsapp2.util.PeriodTime;
import cz.landspa.statsapp2.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class GameServiceImpl implements GameService {


    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final EventRepository eventRepository;
    private final RosterRepository rosterRepository;
    private final ShotsRecordRepository shotsRecordRepository;
    private final SavesRecordRepository savesRecordRepository;

    public GameServiceImpl(GameRepository gameRepository, TeamRepository teamRepository, EventRepository eventRepository, RosterRepository rosterRepository, ShotsRecordRepository shotsRecordRepository, SavesRecordRepository savesRecordRepository) {
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
        this.eventRepository = eventRepository;
        this.rosterRepository = rosterRepository;
        this.shotsRecordRepository = shotsRecordRepository;
        this.savesRecordRepository = savesRecordRepository;
    }

    @Override
    public Game createGame(Game game) {
        if (game.getTeam() == null || game.getTeam().getId() == null) {
            throw new IllegalArgumentException("Tým není vyplněn");
        }

        teamRepository.findById(game.getTeam().getId())
                .orElseThrow(() -> new IllegalArgumentException("Zadaný tým neexistuje"));

        return gameRepository.save(game);
    }

    @Override
    public List<Game> getTeamGames(Long teamId) {

        return gameRepository.findByTeamId(teamId);
    }

    @Override
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    public GameFullInfoDTO getGameWithFullInfoById(Long id) {
        GameFullInfoDTO gameFullInfoDTO = new GameFullInfoDTO();
        Game game = gameRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Zápas nebyl nalezen"));
        checkActiveGoalkeeper(game);
        gameFullInfoDTO.setGame(game);
        gameFullInfoDTO.setRoster(rosterRepository.findAllByGameIdOrderByPlayerPositionSortOrderAscLineAsc(id));
        List<PeriodEvents> events = new ArrayList<>();
        gameFullInfoDTO.setGoalsScored(eventRepository.countGoalsScoredByGameId(id));
        gameFullInfoDTO.setGoalsConceded(eventRepository.countGoalsConcededByGameId(id));
        Map<Integer, PeriodTime> periodTimes = Util.getStartAndEndPeriodTimes(game);
        List<PeriodGoals> periodGoalsList = new ArrayList<>();

        for (int i = 1; i <= periodTimes.size(); i++) {
           PeriodTime time = periodTimes.get(i);
            int startTime = time.startTime();
            int endTime = time.endTime() == null ? Integer.MAX_VALUE : time.endTime();

            Long scored = eventRepository.countGoalsScoredInPeriod(game.getId(), startTime, endTime);
            Long conceded = eventRepository.countGoalsConcededInPeriod(game.getId(), startTime, endTime);
            List<Event> periodEvents = eventRepository.findAllByGameIdAndTimeBetweenOrderByTimeAsc(game.getId(), startTime, endTime);

            PeriodGoals goals = new PeriodGoals(i, scored, conceded);
            periodGoalsList.add(goals);
            events.add(new PeriodEvents((long) i, periodEvents));
        }

        gameFullInfoDTO.setEvents(events);
        gameFullInfoDTO.setPeriodGoals(periodGoalsList);
        checkPeriodStats(game);
        gameFullInfoDTO.setShots(shotsRecordRepository.findByGameId(id));
        gameFullInfoDTO.setSavesRecords(savesRecordRepository.findByGameId(id));

        return gameFullInfoDTO;
    }

    @Override
    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new IllegalArgumentException("Zápas nebyl nalezen");
        }
        gameRepository.deleteById(id);
    }

    @Override
    public Game updateGame(Game game, Long id) {
        Game foundGame = gameRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Zápas nebyl nalezen"));

            if(game.getOpponent()!=null){
                foundGame.setOpponent(game.getOpponent());
            }
            if(game.getDate()!=null){
                foundGame.setDate(game.getDate());
            }
            if(game.getTime()!=null){
                foundGame.setTime(game.getTime());
            }
            if(game.getVenue()!=null){
                foundGame.setVenue(game.getVenue());
            }


            return gameRepository.save(foundGame);

    }

    @Override
    public Long getNumberOfGamesByUser(Long userId) {

        return gameRepository.countByTeamOwnerId(userId);
    }

    @Override
    public List<Game> get4MostRecentGames(Long userId) {

        return gameRepository.findTop4ByDateIsLessThanEqualAndTeamOwnerId(LocalDate.now(),userId);
    }

    @Override
    public List<Game> get4NextGames(Long userId) {
        return gameRepository.findTop4ByDateGreaterThanEqualAndTeamOwnerIdOrderByDateAsc(LocalDate.now(), userId);
    }

    @Override
    public void addPeriod(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Zápas nebyl nalezen"));

        if (game.getCurrentPeriod() <= game.getPeriods()) {
            game.setCurrentPeriod(game.getCurrentPeriod() + 1);


        gameRepository.save(game);
        }
    }

    @Override
    public void removePeriod(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Zápas nebyl nalezen"));

        if(game.getCurrentPeriod()>1){
            game.setCurrentPeriod(game.getCurrentPeriod() - 1);
            gameRepository.save(game);
        }
    }

    private void checkActiveGoalkeeper(Game game){
        Optional<Roster> activeGK = rosterRepository.findByGameIdAndPlayerPositionCodeAndActiveGk(game.getId(),"GK", true);
        List<Roster> roster = rosterRepository.findAllByGameIdAndPlayerPositionCode(game.getId(), "GK");


        if(activeGK.isEmpty()) {
            if(roster.size() == 1){
                Roster onlyGk = roster.get(0);
                onlyGk.setActiveGk(true);
                rosterRepository.save(onlyGk);
            } else if(!roster.isEmpty()) {
                throw new MissingActiveGoalkeeperException(roster);
            }
        }
    }

    private void checkPeriodStats(Game game) {
        Long gameId = game.getId();
        int currentPeriod = game.getCurrentPeriod();

        if (shotsRecordRepository.findByGameIdAndPeriod(gameId, currentPeriod).isEmpty()) {
            ShotsRecord shots = new ShotsRecord();
            shots.setGame(game);
            shots.setPeriod(currentPeriod);
            shots.setShots(0L);
            shotsRecordRepository.save(shots);
        }

        List<Roster> roster = rosterRepository.findAllByGameIdAndPlayerPositionCode(gameId, "GK");

        roster.stream()
                .filter(Roster::isActiveGk)
                .findFirst()
                .ifPresent(goalie -> {
                    Long goalieId = goalie.getId();

                    boolean exists = savesRecordRepository.getByGameIdAndGoalkeeperIdAndPeriod(gameId, goalieId, currentPeriod).isPresent();
                    if (!exists) {
                        SavesRecord saves = new SavesRecord();
                        saves.setGame(game);
                        saves.setGoalkeeper(goalie);
                        saves.setPeriod(currentPeriod);
                        saves.setSaves(0L);
                        savesRecordRepository.save(saves);
                    }
                });
    }
}
