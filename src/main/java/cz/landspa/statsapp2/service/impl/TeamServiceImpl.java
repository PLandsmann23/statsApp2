package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.model.DTO.game.GameWithScoreDTO;
import cz.landspa.statsapp2.model.DTO.team.TeamDetailDTO;
import cz.landspa.statsapp2.model.DTO.team.TeamWithStatsDTO;
import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.model.entity.Team;
import cz.landspa.statsapp2.repository.GameRepository;
import cz.landspa.statsapp2.repository.PlayerRepository;
import cz.landspa.statsapp2.repository.TeamRepository;
import cz.landspa.statsapp2.service.TeamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TeamServiceImpl implements TeamService {


    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    public TeamServiceImpl(TeamRepository teamRepository, GameRepository gameRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public Team createTeam(Team team) {
        if (team.getName() == null || team.getName().isBlank()) {
            throw new IllegalArgumentException("Název musí být vyplněn");
        }

        return teamRepository.save(team);
    }

    @Override
    public List<TeamWithStatsDTO> getUserTeams(Long userId) {

        return teamRepository.findStatsByOwnerId(userId);
    }

    @Override
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public TeamDetailDTO getTeamDetailById(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Tým nebyl nalezen"));
        List<Player> players = playerRepository.findByTeamId(id);
        List<GameWithScoreDTO> games = gameRepository.findByTeamIdWithScore(id);

        return new TeamDetailDTO(team, players, games);
    }

    @Override
    public void deleteTeam(Long id) {
        Team foundTeam = teamRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Tým nebyl nalezen"));
        teamRepository.deleteById(id);
    }

    @Override
    public Team updateTeam(Long id, Team team) {
        Team foundTeam = teamRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Tým nebyl nalezen"));

            if (team.getName() != null) {
                foundTeam.setName(team.getName());
            }

            return teamRepository.save(foundTeam);

    }

    @Override
    public Long getNumberOfTeamsByUser(Long userId) {
        return teamRepository.countByOwnerId(userId);
    }
}
