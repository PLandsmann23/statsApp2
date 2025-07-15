package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {


        List<Player> findByTeamId(Long teamId);

        Long countByTeamOwnerId(Long userId);


        Long countByTeam(Team team);


        @Query("""
                SELECT p FROM Player p
                WHERE p.team.id = ( SELECT g.team.id FROM Game g WHERE g.id = :gameId)
                AND p.id NOT IN ( SELECT r.player.id FROM Roster r WHERE r.game.id = :gameId)
                ORDER BY p.position.sortOrder ASC
                """)
        List<Player> findPlayersByTeamIdAndNotInRosterForGame(@Param("gameId") Long gameId);


}
