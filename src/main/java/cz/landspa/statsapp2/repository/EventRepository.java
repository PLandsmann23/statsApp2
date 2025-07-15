package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByGameIdOrderByTimeAsc(Long gameId);


    List<Event> findAllByGameIdAndTimeBetweenOrderByTimeAsc(Long gameId, Integer startTime, Integer endTime);



    @Query("SELECT COUNT(g) FROM GoalScored g WHERE g.game.id = :gameId AND g.time BETWEEN :startTime AND :endTime")
    Long countGoalsScoredInPeriod(@Param("gameId") Long gameId, @Param("startTime") Integer startTime, @Param("endTime") Integer endTime);

    @Query("SELECT COUNT(g) FROM GoalConceded g WHERE g.game.id = :gameId AND g.time BETWEEN :startTime AND :endTime")
    Long countGoalsConcededInPeriod(@Param("gameId") Long gameId, @Param("startTime") Integer startTime, @Param("endTime") Integer endTime);

    @Query("SELECT COUNT(g) FROM GoalScored g WHERE g.game.id = :gameId")
    Long countGoalsScoredByGameId(@Param("gameId") Long gameId);

    @Query("SELECT COUNT(g) FROM GoalConceded g WHERE g.game.id = :gameId")
    Long countGoalsConcededByGameId(@Param("gameId") Long gameId);


}
