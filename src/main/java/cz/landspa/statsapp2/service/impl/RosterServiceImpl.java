package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.exception.ApiExceptionHandler;
import cz.landspa.statsapp2.exception.GlobalExceptionHandler;
import cz.landspa.statsapp2.exception.RosterConflictException;
import cz.landspa.statsapp2.model.DTO.game.GameRosterDTO;
import cz.landspa.statsapp2.model.DTO.roster.RosterConflictDTO;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.model.entity.Roster;
import cz.landspa.statsapp2.repository.GameRepository;
import cz.landspa.statsapp2.repository.PlayerRepository;
import cz.landspa.statsapp2.repository.RosterRepository;
import cz.landspa.statsapp2.service.RosterService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class RosterServiceImpl implements RosterService {

    private final RosterRepository rosterRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public RosterServiceImpl(RosterRepository rosterRepository, GameRepository gameRepository, PlayerRepository playerRepository) {
        this.rosterRepository = rosterRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public Roster addPlayerToRoster(Roster roster) {
        Game game = gameRepository.findById(roster.getGame().getId())
                .orElseThrow(() -> new IllegalArgumentException("Zápas nebyl nalezen"));

        Player player = playerRepository.findById(roster.getPlayer().getId())
                .orElseThrow(() -> new IllegalArgumentException("Hráč nebyl nalezen"));

        boolean exists = rosterRepository.existsByGameIdAndPlayerId(game.getId(), player.getId());
        if (exists) {
            throw new IllegalArgumentException("Hráč už je na soupisce pro tento zápas");
        }

        if ("GK".equals(player.getPosition().getCode()) &&
                rosterRepository.countByGameIdAndPlayerPositionCode(game.getId(), "GK") >= 2L) {
            throw new IllegalArgumentException("Na soupisce se již nachází dva brankáři");
        }


        Integer number = player.getNumber();

        Optional<Roster> conflict = rosterRepository.findByGameIdAndGameNumber(game.getId(), number);
        if (conflict.isPresent()) {
            throw new RosterConflictException(new RosterConflictDTO(player,null, conflict.get(), number));
        }

        roster.setGame(game);
        roster.setPlayer(player);
        if(roster.getGameNumber()==null)roster.setGameNumber(number);

        return rosterRepository.save(roster);
    }

    @Override
    public List<Roster> getRosterForGame(Long gameId) {
        return rosterRepository.findAllByGameIdOrderByPlayerPositionSortOrderAscLineAsc(gameId);
    }

    @Override
    public GameRosterDTO getRosterWithInfoForGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(()-> new IllegalArgumentException("Zápas nebyl nalezen"));
        List<Roster> roster = rosterRepository.findAllByGameIdOrderByPlayerPositionSortOrderAscLineAsc(gameId);
        List<Player> notInRoster = playerRepository.findPlayersByTeamIdAndNotInRosterForGame(gameId);

        return new GameRosterDTO(game, roster,notInRoster);
    }

    @Override
    public void deleteRoster(Long id) {
        if (!rosterRepository.existsById(id)) {
            throw new IllegalArgumentException("Záznam nebyl nalezen");
        }

        rosterRepository.deleteById(id);
    }

    @Override
    public Roster updateRoster(Roster updatedRoster, Long id) {
        Roster existing = rosterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soupiska nenalezena"));

        Integer newNumber = updatedRoster.getGameNumber();

        if (newNumber != null && !newNumber.equals(existing.getGameNumber())) {
            Optional<Roster> conflict = rosterRepository
                    .findByGameIdAndGameNumber(existing.getGame().getId(), newNumber);

            if (conflict.isPresent() && !conflict.get().getId().equals(id)) {
                throw new RosterConflictException(new RosterConflictDTO(
                        null,existing, conflict.get(), newNumber));
            }

            existing.setGameNumber(newNumber);
        }

        if (updatedRoster.getLine() != null) {
            existing.setLine(updatedRoster.getLine());
        }
        existing.setActiveGk(updatedRoster.isActiveGk());

        return rosterRepository.save(existing);
    }

    @Override
    public void changeActiveGoalkeeper(Long gameId) {
        List<Roster> activeGoalies = rosterRepository.findAllByGameIdAndPlayerPositionCodeAndActiveGk(gameId, "GK", true);
        if (activeGoalies.size() != 1) {
            throw new IllegalStateException("Nelze jednoznačně určit aktivního brankáře");
        }

        Roster leaving = rosterRepository
                .findByGameIdAndPlayerPositionCodeAndActiveGk(gameId, "GK", true)
                .orElseThrow(() -> new IllegalArgumentException("Odcházející brankář pro zápas ID " + gameId + " nenalezen"));

        Roster coming = rosterRepository
                .findByGameIdAndPlayerPositionCodeAndActiveGk(gameId, "GK", false)
                .orElseThrow(() -> new IllegalArgumentException("Přicházející brankář pro zápas ID " + gameId + " nenalezen"));

        leaving.setActiveGk(false);
        coming.setActiveGk(true);

        rosterRepository.saveAll(List.of(leaving, coming));
    }


    @Override
    public Optional<Roster> getRosterById(Long id) {
        return rosterRepository.findById(id);
    }
}
