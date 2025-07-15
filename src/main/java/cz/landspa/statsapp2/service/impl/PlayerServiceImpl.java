package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.model.entity.Team;
import cz.landspa.statsapp2.repository.PlayerRepository;
import cz.landspa.statsapp2.repository.TeamRepository;
import cz.landspa.statsapp2.service.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Player createPlayer(Player player) {
        if (player.getTeam() == null || player.getTeam().getId() == null) {
            throw new IllegalArgumentException("Tým hráče není vyplněn");
        }

        teamRepository.findById(player.getTeam().getId())
                .orElseThrow(() -> new IllegalArgumentException("Zadaný tým neexistuje"));

        return playerRepository.save(player);
    }

    @Override
    public List<Player> getTeamPlayers(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hráč nebyl nalezen"));
    }

    @Override
    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new IllegalArgumentException("Hráč nebyl nalezen");
        }
        playerRepository.deleteById(id);
    }

    @Override
    public Player updatePlayer(Player updatedPlayer, Long id) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hráč nebyl nalezen"));

        if (updatedPlayer.getName() != null) existingPlayer.setName(updatedPlayer.getName());
        if (updatedPlayer.getSurname() != null) existingPlayer.setSurname(updatedPlayer.getSurname());
        if (updatedPlayer.getNumber() != null) existingPlayer.setNumber(updatedPlayer.getNumber());
        if (updatedPlayer.getPosition() != null) existingPlayer.setPosition(updatedPlayer.getPosition());
        if (updatedPlayer.getTeam() != null) {
            Long teamId = updatedPlayer.getTeam().getId();
            Team team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new IllegalArgumentException("Zadaný tým neexistuje"));
            existingPlayer.setTeam(team);
        }

        return playerRepository.save(existingPlayer);
    }

    @Override
    public Long getNumberOfPlayersByUser(Long userId) {
        return playerRepository.countByTeamOwnerId(userId);
    }

    @Override
    public List<Player> findTeamPlayersNotInRoster(Long gameId) {
        return playerRepository.findPlayersByTeamIdAndNotInRosterForGame(gameId);
    }

}
