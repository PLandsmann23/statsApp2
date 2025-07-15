package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.DTO.user.UserInfo;
import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.service.GameService;
import cz.landspa.statsapp2.service.PlayerService;
import cz.landspa.statsapp2.service.TeamService;
import cz.landspa.statsapp2.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class InfoController {


    private final SecurityUtil securityUtil;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final GameService gameService;

    public InfoController(SecurityUtil securityUtil, TeamService teamService, PlayerService playerService, GameService gameService) {
        this.securityUtil = securityUtil;
        this.teamService = teamService;
        this.playerService = playerService;
        this.gameService = gameService;
    }


    @GetMapping
    public ResponseEntity<UserInfo> getInfo (){
        User user = securityUtil.getCurrentUser();

        UserInfo userInfo = new UserInfo();

        userInfo.setNextGames(gameService.get4NextGames(user.getId()));
        userInfo.setTeams(teamService.getUserTeams(user.getId()));
        userInfo.setNoOfGames(gameService.getNumberOfGamesByUser(user.getId()));
        userInfo.setNoOfTeams(teamService.getNumberOfTeamsByUser(user.getId()));
        userInfo.setNoOfPlayers(playerService.getNumberOfPlayersByUser(user.getId()));

        return ResponseEntity.ok(userInfo);

    }

}
