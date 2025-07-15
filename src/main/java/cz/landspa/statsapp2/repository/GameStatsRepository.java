package cz.landspa.statsapp2.repository;


import cz.landspa.statsapp2.model.DTO.stats.game.GoalkeeperGameStats;
import cz.landspa.statsapp2.model.DTO.stats.game.PlayerGameStats;
import cz.landspa.statsapp2.model.entity.Roster;
import cz.landspa.statsapp2.model.entity.stats.SavesRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GameStatsRepository {

    @PersistenceContext
    private EntityManager em;

    public List<PlayerGameStats> getPlayerStats(Long gameId) {
        return em.createQuery("""
        SELECT new cz.landspa.statsapp2.model.DTO.stats.game.PlayerGameStats(
                             r,
                             COALESCE((SELECT COUNT(DISTINCT gs) * 1L FROM GoalScored gs WHERE gs.game.id = :gameId AND gs.scorer = r), 0L),
                             COALESCE((SELECT COUNT(DISTINCT a) * 1L FROM GoalScored g JOIN g.assists a WHERE g.game.id = :gameId AND a = r), 0L),
                             COALESCE((SELECT COUNT(DISTINCT o) * 1L FROM GoalScored g JOIN g.onIce o WHERE g.game.id = :gameId AND o = r), 0L),
                             COALESCE((SELECT COUNT(DISTINCT o) * 1L FROM GoalConceded gc JOIN gc.onIce o WHERE gc.game.id = :gameId AND o = r), 0L),
                             COALESCE((SELECT SUM(p.minutes) * 1L FROM Penalty p WHERE p.game.id = :gameId AND p.player = r), 0L)
                         )
                         FROM Roster r
                         WHERE r.game.id = :gameId
                         AND r.player.position.code <> "GK"
                """, PlayerGameStats.class)
                .setParameter("gameId", gameId)
                .getResultList();
    }

    public List<GoalkeeperGameStats> getGoalkeeperStats(Long gameId) {
        // Vrátí soupisku s aktivními GK v zápase
        List<Roster> gkList = em.createQuery("""
            SELECT r FROM Roster r
            WHERE r.game.id = :gameId AND r.player.position.code = 'GK'
        """, Roster.class)
                .setParameter("gameId", gameId)
                .getResultList();

        List<GoalkeeperGameStats> result = new ArrayList<>();

        for (Roster gk : gkList) {
            // Najdi SavesRecord pro každou třetinu
            List<SavesRecord> saves = em.createQuery("""
                SELECT s FROM SavesRecord s
                WHERE s.game.id = :gameId AND s.goalkeeper.id = :goalieId
            """, SavesRecord.class)
                    .setParameter("gameId", gameId)
                    .setParameter("goalieId", gk.getId())
                    .getResultList();

            // Spočítej inkasované góly
            Long goalsConceded = em.createQuery("""
                SELECT COUNT(gc) FROM GoalConceded gc
                WHERE gc.game.id = :gameId AND gc.inGoal.id = :goalieId
            """, Long.class)
                    .setParameter("gameId", gameId)
                    .setParameter("goalieId", gk.getId())
                    .getSingleResult();

            result.add(new GoalkeeperGameStats(gk, saves, goalsConceded));
        }

        return result;
    }
}

