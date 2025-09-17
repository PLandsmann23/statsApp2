package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.DTO.team.TeamDetailDTO;
import cz.landspa.statsapp2.model.DTO.team.TeamWithStatsDTO;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.model.entity.Team;
import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.service.GameService;
import cz.landspa.statsapp2.service.PlayerService;
import cz.landspa.statsapp2.service.TeamService;
import cz.landspa.statsapp2.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {


    private final SecurityUtil securityUtil;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final GameService gameService;

    public TeamController(SecurityUtil securityUtil, TeamService teamService, PlayerService playerService, GameService gameService) {
        this.securityUtil = securityUtil;
        this.teamService = teamService;
        this.playerService = playerService;
        this.gameService = gameService;
    }


    @GetMapping
    public ResponseEntity<List<TeamWithStatsDTO>> getUserTeams (){
        List<TeamWithStatsDTO> teams = teamService.getUserTeams(securityUtil.getCurrentUser().getId());

        return ResponseEntity.ok(teams);

    }

    @PostMapping
    public ResponseEntity<Team> newTeam(@Valid @RequestBody Team team){

            User foundUser = securityUtil.getCurrentUser();
            team.setOwner(foundUser);

            Team newTeam = teamService.createTeam(team);

            return ResponseEntity.created(URI.create("/api/teams/" + newTeam.getId())).body(newTeam);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteTeam (@PathVariable Long id){

            teamService.deleteTeam(id);

        return ResponseEntity.ok(Map.of("message", "Tým byl úspěšně smazán"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> editTeam (@PathVariable Long id, @Valid @RequestBody Team team){
        Team updatedTeam = teamService.updateTeam(id,team);

        return ResponseEntity.ok(updatedTeam);

    }

    @GetMapping("/{id}/players")
    public ResponseEntity<?> getTeamPlayers (@PathVariable Long id){
        List<Player> players = playerService.getTeamPlayers(id);

        return ResponseEntity.ok(players);

    }

    @PostMapping("/{id}/players")
    public ResponseEntity<Player> createPlayer(@PathVariable Long id, @Valid @RequestBody Player player) {
        Team team = teamService.getTeamById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tým nebyl nalezen"));
        player.setTeam(team);
        Player saved = playerService.createPlayer(player);
        return ResponseEntity.created(URI.create("/api/players/" + saved.getId())).body(saved);
    }

    @PostMapping("/{teamId}/games")
    public ResponseEntity<Game> createGame(@PathVariable Long teamId, @RequestBody Game game) {
        Team team = teamService.getTeamById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Tým nebyl nalezen"));

        game.setTeam(team);
        Game newGame = gameService.createGame(game);

        return ResponseEntity.created(URI.create("/api/games/" + newGame.getId()))
                .body(newGame);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam (@PathVariable Long id){
        Team team = teamService.getTeamById(id).orElseThrow(()->new IllegalArgumentException("Tým nebyl nalezen"));

        return ResponseEntity.ok(team);

    }

    @GetMapping("/{id}/games")
    public ResponseEntity<?> getTeamGames (@PathVariable Long id){
        List<Game> games = gameService.getTeamGames(id);

        return ResponseEntity.ok(games);

    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<TeamDetailDTO> getTeamDetail (@PathVariable Long id){
        TeamDetailDTO team = teamService.getTeamDetailById(id);

        return ResponseEntity.ok(team);

    }


}
