package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.DTO.team.TeamDetailDTO;
import cz.landspa.statsapp2.model.DTO.team.TeamWithStatsDTO;
import cz.landspa.statsapp2.model.entity.Team;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TeamService {
    Team createTeam(Team team);

    List<TeamWithStatsDTO> getUserTeams(Long userId);

    Optional<Team> getTeamById(Long id);

    TeamDetailDTO getTeamDetailById(Long id);

    void deleteTeam (Long id);

    Team updateTeam(Long id, Team team);

    Long getNumberOfTeamsByUser(Long userId);

 //   List<TeamInfo> getTeamInfo(Long userId);


 //   TeamInfo getOneTeamInfo(Long teamId);

}
