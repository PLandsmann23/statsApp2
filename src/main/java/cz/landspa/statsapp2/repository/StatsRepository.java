package cz.landspa.statsapp2.repository;


import cz.landspa.statsapp2.model.DTO.stats.GoalkeeperStats;
import cz.landspa.statsapp2.model.DTO.stats.PlayerStats;
import cz.landspa.statsapp2.model.DTO.stats.TeamStats;
import cz.landspa.statsapp2.model.DTO.stats.game.GameStats;
import cz.landspa.statsapp2.model.DTO.stats.game.PeriodStats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class StatsRepository {

    @PersistenceContext
    private EntityManager em;

    public List<PlayerStats> getPlayerStats(Long teamId, LocalDate start, LocalDate end, Pageable pageable) {
        return em.createQuery("""
        SELECT new cz.landspa.statsapp2.model.DTO.stats.PlayerStats(
            r.player,
            COALESCE((SELECT COUNT(DISTINCT r2.game.id) * 1L FROM Roster r2 WHERE r2.player = r.player  AND (:start IS NULL OR r2.game.date >= :start) AND (:end IS NULL OR r2.game.date <= :end)), 0L),
            COALESCE((SELECT COUNT(gs) * 1L FROM GoalScored gs WHERE gs.scorer = r  AND (:start IS NULL OR gs.game.date >= :start) AND (:end IS NULL OR gs.game.date <= :end)), 0L),
            COALESCE((SELECT COUNT(a) * 1L FROM GoalScored gs JOIN gs.assists a WHERE a = r  AND (:start IS NULL OR gs.game.date >= :start) AND (:end IS NULL OR gs.game.date <= :end)), 0L),
            COALESCE((SELECT COUNT(o) * 1L FROM GoalScored gs JOIN gs.onIce o WHERE o = r  AND (:start IS NULL OR gs.game.date >= :start) AND (:end IS NULL OR gs.game.date <= :end)), 0L),
            COALESCE((SELECT COUNT(o) * 1L FROM GoalConceded gc JOIN gc.onIce o WHERE o = r  AND (:start IS NULL OR gc.game.date >= :start) AND (:end IS NULL OR gc.game.date <= :end)), 0L),
            COALESCE((SELECT SUM(p.minutes) * 1L FROM Penalty p WHERE p.player = r  AND (:start IS NULL OR p.game.date >= :start) AND (:end IS NULL OR p.game.date <= :end)), 0L)
        )
        FROM Roster r
        WHERE r.player.team.id = :teamId
        AND r.player.position.code <> 'GK'
        GROUP BY r.player
        ORDER BY r.player.name
    """, PlayerStats.class)
                .setParameter("teamId", teamId)
                .setParameter("start", start)
                .setParameter("end", end)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }




    public List<GoalkeeperStats> getGoalkeeperStats(Long teamId, LocalDate start, LocalDate end, Pageable pageable) {
        return em.createQuery("""
        SELECT new cz.landspa.statsapp2.model.DTO.stats.GoalkeeperStats(
            r.player,
            COALESCE((
                   SELECT COUNT(DISTINCT s.game.id)
                   FROM SavesRecord s
                   WHERE s.goalkeeper.player = r.player
                     AND s.goalkeeper.player.position.code = 'GK'
                     AND s.goalkeeper.player.team.id = :teamId
                     AND (:start IS NULL OR s.game.date >= :start)
                     AND (:end IS NULL OR s.game.date <= :end)
               ), 0L),

            COALESCE((SELECT COUNT(gc) * 1L
                      FROM GoalConceded gc
                      WHERE gc.inGoal = r
                         AND (:start IS NULL OR gc.game.date >= :start)
              AND (:end IS NULL OR gc.game.date <= :end)), 0L),

            COALESCE((SELECT SUM(s.saves) * 1L
                      FROM SavesRecord s
                      WHERE s.goalkeeper = r
                       AND (:start IS NULL OR s.game.date >= :start)
              AND (:end IS NULL OR s.game.date <= :end)), 0L)
        )
        FROM Roster r
        WHERE r.player.team.id = :teamId
          AND r.player.position.code = 'GK'
        GROUP BY r.player
        ORDER BY r.player.number
    """, GoalkeeperStats.class)
                .setParameter("teamId", teamId)
                .setParameter("start", start)
                .setParameter("end", end)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }


    public TeamStats getTeamStats(Long teamId, LocalDate start, LocalDate end, Pageable pageable) {
        List<Long> gameIds = em.createQuery("""
            SELECT g.id FROM Game g
            WHERE g.team.id = :teamId
              AND (:start IS NULL OR g.date >= :start)
              AND (:end IS NULL OR g.date <= :end)
            ORDER BY g.date DESC
        """, Long.class)
                .setParameter("teamId", teamId)
                .setParameter("start", start)
                .setParameter("end", end)
                .setMaxResults(pageable != null ? pageable.getPageSize() : Integer.MAX_VALUE)
                .getResultList();

        if (gameIds.isEmpty()) {
            return new TeamStats(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        }

        Long ppGoals = em.createQuery("""
    SELECT COUNT(DISTINCT gs.id) FROM GoalScored gs
    WHERE gs.game.id IN :gameIds AND gs.situation IN ('5/4', '5/3', '4/3')
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long ppMajorGoals = em.createQuery("""
    SELECT COUNT(DISTINCT gs.id) FROM GoalScored gs
    WHERE gs.game.id IN :gameIds AND gs.situation IN ('5/4', '5/3', '4/3') AND gs.majorPenalty = true
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long ppNumberMinorMajor = em.createQuery("""
    SELECT COUNT(DISTINCT op.id) FROM OpponentPenalty op
    WHERE op.game.id IN :gameIds AND op.minutes IN (2, 5, 25, 12) AND op.coincidental = false
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long ppNumberDoubleMinor = em.createQuery("""
    SELECT COUNT(DISTINCT op.id) FROM OpponentPenalty op
    WHERE op.game.id IN :gameIds AND op.minutes = 4 AND op.coincidental = false
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long ppNumber = ppNumberMinorMajor + (ppNumberDoubleMinor * 2);

        Long shGoals = em.createQuery("""
    SELECT COUNT(DISTINCT gc.id) FROM GoalConceded gc
    WHERE gc.game.id IN :gameIds AND gc.situation IN ('4/5', '3/5', '3/4')
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long shMajorGoals = em.createQuery("""
    SELECT COUNT(DISTINCT gc.id) FROM GoalConceded gc
    WHERE gc.game.id IN :gameIds AND gc.situation IN ('4/5', '3/5', '3/4') AND gc.majorPenalty = true
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long shNumberMinorMajor = em.createQuery("""
    SELECT COUNT(DISTINCT p.id) FROM Penalty p
    WHERE p.game.id IN :gameIds AND p.minutes IN (2, 5, 25, 12) AND p.coincidental = false
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long shNumberDoubleMinor = em.createQuery("""
    SELECT COUNT(DISTINCT p.id) FROM Penalty p
    WHERE p.game.id IN :gameIds AND p.minutes = 4 AND p.coincidental = false
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long shNumber = shNumberMinorMajor + (shNumberDoubleMinor * 2);

        Long totalShots = em.createQuery("""
    SELECT SUM(s.shots) FROM ShotsRecord s WHERE s.game.id IN :gameIds
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long totalSaves = em.createQuery("""
    SELECT SUM(sa.saves) FROM SavesRecord sa WHERE sa.game.id IN :gameIds
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long totalGames = (long) gameIds.size();

        Long totalGoalsScored = em.createQuery("""
    SELECT COUNT(DISTINCT gs.id) FROM GoalScored gs WHERE gs.game.id IN :gameIds
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long totalGoalsConceded = em.createQuery("""
    SELECT COUNT(DISTINCT gc.id) FROM GoalConceded gc WHERE gc.game.id IN :gameIds
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        Long totalGoalsConcededNoEmpty = em.createQuery("""
    SELECT COUNT(DISTINCT gc.id) FROM GoalConceded gc WHERE gc.game.id IN :gameIds AND gc.inGoal IS NOT NULL
""", Long.class)
                .setParameter("gameIds", gameIds)
                .getSingleResult();

        return new TeamStats(
                ppGoals,
                ppMajorGoals,
                ppNumber,
                shGoals,
                shMajorGoals,
                shNumber,
                totalShots != null ? totalShots : 0L,
                totalSaves != null ? totalSaves : 0L,
                totalGames,
                totalGoalsScored != null ? totalGoalsScored : 0L,
                totalGoalsConceded != null ? totalGoalsConceded : 0L,
                totalGoalsConcededNoEmpty != null ? totalGoalsConcededNoEmpty : 0L
        );

    }

    public  GameStats getGameTeamStats(Long gameId) {

        Long teamGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs WHERE gs.game.id = :gameId
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long opponentGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc WHERE gc.game.id = :gameId
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long teamShots = em.createQuery("""
            SELECT SUM (s.shots) FROM ShotsRecord s WHERE s.game.id = :gameId
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long teamSaves = em.createQuery("""
            SELECT SUM(s.saves) FROM SavesRecord s WHERE s.game.id = :gameId
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long opponentShots = opponentGoals + teamSaves;
        Long opponentSaves = opponentGoals != null && teamShots != null ? teamShots - opponentGoals : null;

        Long opponentMinorMajorPP = em.createQuery("""
            SELECT COUNT (DISTINCT p.id) FROM Penalty p WHERE p.game.id = :gameId
            AND p.minutes IN (2,12,5,25)
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long opponentDoubleMinorPP = em.createQuery("""
            SELECT COUNT (DISTINCT p.id) FROM Penalty p WHERE p.game.id = :gameId
                        AND p.minutes = 4
        """, Long.class).setParameter("gameId", gameId).getSingleResult();


        Long teamMinorMajorPP = em.createQuery("""
            SELECT COUNT (DISTINCT p.id) FROM OpponentPenalty p WHERE p.game.id = :gameId
            AND p.minutes IN (2,12,5,25)
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long teamDoubleMinorPP = em.createQuery("""
            SELECT COUNT (DISTINCT p.id) FROM OpponentPenalty p WHERE p.game.id = :gameId
                        AND p.minutes = 4
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long teamPowerPlays = teamMinorMajorPP + teamDoubleMinorPP*2;
        
        Long opponentPowerPlays =  opponentMinorMajorPP + opponentDoubleMinorPP*2;

        Long teamPPGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs WHERE gs.game.id = :gameId AND gs.situation IN ('5/4','5/3','4/3')
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long opponentPPGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc WHERE gc.game.id = :gameId AND gc.situation IN ('4/5','3/5','3/4')
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long teamSHGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs WHERE gs.game.id = :gameId AND gs.situation IN ('4/5','3/5','3/4')
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long opponentSHGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc WHERE gc.game.id = :gameId AND gc.situation IN ('5/4','5/3','4/3')
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long teamPPMajorGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs WHERE gs.game.id = :gameId AND gs.majorPenalty = true AND gs.situation IN ('5/4','5/3','4/3')
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long opponentPPMajorGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc WHERE gc.game.id = :gameId AND gc.majorPenalty = true AND gc.situation IN ('4/5','3/5','3/4')
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long teamSHMajorGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs WHERE gs.game.id = :gameId AND gs.majorPenalty = true AND gs.situation IN ('4/5','3/5','3/4')
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        Long opponentSHMajorGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc WHERE gc.game.id = :gameId AND gc.majorPenalty = true AND gc.situation IN ('5/4','5/3','4/3')
        """, Long.class).setParameter("gameId", gameId).getSingleResult();

        return new GameStats(
                teamGoals, opponentGoals,
                teamSaves, opponentSaves,
                teamShots, opponentShots,
                teamPowerPlays, opponentPowerPlays,
                teamPPGoals, opponentPPGoals,
                teamSHGoals, opponentSHGoals,
                teamPPMajorGoals, opponentPPMajorGoals
        );
    }

    public PeriodStats getPeriodTeamStats(Long gameId, Integer startTime, Integer endTime, Integer period) {

        Long teamGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs
            WHERE gs.game.id = :gameId AND gs.time BETWEEN :start AND :end
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long opponentGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc
            WHERE gc.game.id = :gameId AND gc.time BETWEEN :start AND :end
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long teamShots = em.createQuery("""
            SELECT s.shots FROM ShotsRecord s
            WHERE s.game.id = :gameId AND s.period = :period
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("period", period)
                .getSingleResult();

        Long teamSaves = em.createQuery("""
            SELECT SUM(s.saves) FROM SavesRecord s
            WHERE s.game.id = :gameId AND s.period = :period
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("period", period)
                .getSingleResult();

        Long opponentShots = opponentGoals + teamSaves;
        Long opponentSaves = teamShots != null ? teamShots - opponentGoals : null;

        Long opponentMinorMajorPP = em.createQuery("""
            SELECT COUNT (DISTINCT p.id) FROM Penalty p WHERE p.game.id = :gameId AND p.time BETWEEN :start AND :end
            AND p.minutes IN (2,12,5,25)
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime).getSingleResult();

        Long opponentDoubleMinorPP = em.createQuery("""
            SELECT COUNT (DISTINCT p.id) FROM Penalty p WHERE p.game.id = :gameId AND p.time BETWEEN :start AND :end
                        AND p.minutes = 4
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime).getSingleResult();


        Long teamMinorMajorPP = em.createQuery("""
            SELECT COUNT (DISTINCT p.id) FROM OpponentPenalty p WHERE p.game.id = :gameId AND p.time BETWEEN :start AND :end
            AND p.minutes IN (2,12,5,25)
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime).getSingleResult();

        Long teamDoubleMinorPP = em.createQuery("""
            SELECT COUNT (DISTINCT p.id) FROM OpponentPenalty p WHERE p.game.id = :gameId AND p.time BETWEEN :start AND :end
                        AND p.minutes = 4
        """, Long.class).setParameter("gameId", gameId).setParameter("start", startTime)
                .setParameter("end", endTime).getSingleResult();

        Long teamPowerPlays = teamMinorMajorPP + teamDoubleMinorPP*2;

        Long opponentPowerPlays =  opponentMinorMajorPP + opponentDoubleMinorPP*2;

        Long teamPPGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs
            WHERE gs.game.id = :gameId AND gs.time BETWEEN :start AND :end
            AND gs.situation IN ('5/4','5/3','4/3')
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long opponentPPGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc
            WHERE gc.game.id = :gameId AND gc.time BETWEEN :start AND :end
            AND gc.situation IN ('4/5','3/5','3/4')
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long teamSHGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs
            WHERE gs.game.id = :gameId AND gs.time BETWEEN :start AND :end
            AND gs.situation IN ('4/5','3/5','3/4')
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long opponentSHGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc
            WHERE gc.game.id = :gameId AND gc.time BETWEEN :start AND :end
            AND gc.situation IN ('5/4','5/3','4/3')
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long teamPPMajorGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs
            WHERE gs.game.id = :gameId AND gs.time BETWEEN :start AND :end
            AND gs.majorPenalty = true AND gs.situation IN ('5/4','5/3','4/3')
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long opponentPPMajorGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc
            WHERE gc.game.id = :gameId AND gc.time BETWEEN :start AND :end
            AND gc.majorPenalty = true AND gc.situation IN ('4/5','3/5','3/4')
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long teamSHMajorGoals = em.createQuery("""
            SELECT COUNT(gs.id) FROM GoalScored gs
            WHERE gs.game.id = :gameId AND gs.time BETWEEN :start AND :end
            AND gs.majorPenalty = true AND gs.situation IN ('4/5','3/5','3/4')
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();

        Long opponentSHMajorGoals = em.createQuery("""
            SELECT COUNT(gc.id) FROM GoalConceded gc
            WHERE gc.game.id = :gameId AND gc.time BETWEEN :start AND :end
            AND gc.majorPenalty = true AND gc.situation IN ('5/4','5/3','4/3')
        """, Long.class).setParameter("gameId", gameId)
                .setParameter("start", startTime)
                .setParameter("end", endTime)
                .getSingleResult();


        return new PeriodStats(
                teamGoals, opponentGoals,
                teamSaves, opponentSaves,
                teamShots, opponentShots,
                teamPowerPlays, opponentPowerPlays,
                teamPPGoals, opponentPPGoals,
                teamSHGoals, opponentSHGoals,
                teamPPMajorGoals, opponentPPMajorGoals
        );
    }
}




