package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.DTO.game.GameWithScoreDTO;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByTeamId(Long teamId);


    @Query("SELECT new cz.landspa.statsapp2.model.DTO.game.GameWithScoreDTO(g, COUNT (DISTINCT (gs)),COUNT (DISTINCT (gc))) FROM Game g LEFT JOIN GoalScored gs ON gs.game = g LEFT JOIN GoalConceded gc ON gc.game = g WHERE g.team.id = :teamId GROUP BY g.id")
    List<GameWithScoreDTO> findByTeamIdWithScore(Long teamId);

    Long countByTeamOwnerId(Long ownerId);

    Long countByTeam(Team team);


    List<Game> findTop4ByDateIsLessThanEqualAndTeamOwnerId(LocalDate date, Long ownerId);

    List<Game> findTop4ByDateGreaterThanEqualAndTeamOwnerIdOrderByDateAsc(LocalDate date, Long ownerId);
}
