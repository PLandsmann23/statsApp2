package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.DTO.team.TeamWithStatsDTO;
import cz.landspa.statsapp2.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {


    List<Team> findByOwnerId(Long ownerId);


    @Query("SELECT new cz.landspa.statsapp2.model.DTO.team.TeamWithStatsDTO(t.id, t.name, COUNT (DISTINCT (p)),COUNT (DISTINCT (g))) FROM Team t LEFT JOIN Player p ON p.team = t LEFT JOIN Game g ON g.team = t WHERE t.owner.id = :ownerId GROUP BY t.id, t.name")
    List<TeamWithStatsDTO> findStatsByOwnerId(Long ownerId);



    Long countByOwnerId(Long ownerId);



}
