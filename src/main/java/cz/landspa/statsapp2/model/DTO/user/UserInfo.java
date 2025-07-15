package cz.landspa.statsapp2.model.DTO.user;

import cz.landspa.statsapp2.model.DTO.team.TeamWithStatsDTO;
import cz.landspa.statsapp2.model.entity.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserInfo {

    List<Game> nextGames;

    List<TeamWithStatsDTO> teams;

    Long noOfGames;

    Long noOfPlayers;

    Long noOfTeams;
}
