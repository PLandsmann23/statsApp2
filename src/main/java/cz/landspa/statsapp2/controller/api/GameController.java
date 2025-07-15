package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.DTO.event.*;
import cz.landspa.statsapp2.model.DTO.game.GameFullInfoDTO;
import cz.landspa.statsapp2.model.DTO.game.GameRosterDTO;
import cz.landspa.statsapp2.model.DTO.roster.RosterNumberUpdateDTO;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Roster;
import cz.landspa.statsapp2.model.entity.event.Event;
import cz.landspa.statsapp2.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final RosterService rosterService;
    private final EventService eventService;
    private final PlayerService playerService;


    public GameController(GameService gameService, RosterService rosterService, EventService eventService, PlayerService playerService) {
        this.gameService = gameService;
        this.rosterService = rosterService;
        this.eventService = eventService;
        this.playerService = playerService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<GameFullInfoDTO> getGameWithFullInfo(@PathVariable Long id){
        GameFullInfoDTO gameFullInfoDTO = gameService.getGameWithFullInfoById(id);

        return ResponseEntity.ok(gameFullInfoDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @Valid @RequestBody Game game){
        Game updated = gameService.updateGame(game, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id){
            gameService.deleteGame(id);
        return ResponseEntity.ok(Map.of("message", "Zápas byl úspěšně smazán"));
    }


    @PostMapping("/{id}/addPeriod")
    public ResponseEntity<GameFullInfoDTO> addPeriod(@PathVariable Long id) {
        gameService.addPeriod(id);
        GameFullInfoDTO updated = gameService.getGameWithFullInfoById(id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/removePeriod")
    public ResponseEntity<GameFullInfoDTO> removePeriod(@PathVariable Long id) {
        gameService.removePeriod(id);
        GameFullInfoDTO updated = gameService.getGameWithFullInfoById(id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/changeGoalkeeper")
    public ResponseEntity<?> changeGoalkeeper(@PathVariable Long id) {
        rosterService.changeActiveGoalkeeper(id);
        return ResponseEntity.ok(Map.of("message", "Brankáři změněni"));
    }

    @GetMapping("/{id}/roster")
    public ResponseEntity<GameRosterDTO> getRoster(@PathVariable Long id) {
        return ResponseEntity.ok(rosterService.getRosterWithInfoForGame(id));
    }

    @PostMapping("/{id}/roster")
    public ResponseEntity<Roster> addPlayerToRoster(@PathVariable Long id, @Valid @RequestBody Roster roster) {
        Game game = gameService.getGameById(id).orElseThrow(()-> new IllegalArgumentException("Zápas nebyl nalezen"));

        roster.setGame(game);
        Roster saved = rosterService.addPlayerToRoster(roster);
        return ResponseEntity.created(URI.create("/api/roster/" + saved.getId())).body(saved);
    }

    @PatchMapping("/{id}/roster/conflict")
    public ResponseEntity<?> resolveNumberConflict(@PathVariable Long id,
                                                   @RequestBody List<RosterNumberUpdateDTO> updates) {

        for (RosterNumberUpdateDTO update : updates) {
            if (update.getRosterId() != null) {
                Roster roster = rosterService.getRosterById(update.getRosterId())
                        .orElseThrow(() -> new IllegalArgumentException("Roster nenalezen"));
                roster.setGameNumber(update.getNewNumber());
                rosterService.updateRoster(roster, roster.getId());
            } else if (update.getPlayerId() != null) {
                Roster roster = new Roster();
                roster.setGame(gameService.getGameById(id).orElseThrow());
                roster.setPlayer(playerService.getPlayerById(update.getPlayerId()));
                roster.setGameNumber(update.getNewNumber());
                rosterService.addPlayerToRoster(roster);
            }
        }

        return ResponseEntity.ok(Map.of("message", "Konflikt čísel vyřešen"));
    }


    @PostMapping("/{id}/goalScored")
    public ResponseEntity<Event> addGoal(@PathVariable Long id, @RequestBody GoalScoredDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addGoal(id, dto));
    }

    @PostMapping("/{id}/goalConceded")
    public ResponseEntity<Event> addGoalConceded(@PathVariable Long id, @RequestBody GoalConcededDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addConcededGoal(id, dto));
    }

    @PostMapping("/{id}/penalty")
    public ResponseEntity<Event> addPenalty(@PathVariable Long id, @RequestBody PenaltyDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addPenalty(id, dto));
    }

    @PostMapping("/{id}/opponentPenalty")
    public ResponseEntity<Event> addOpponentPenalty(@PathVariable Long id, @RequestBody OpponentPenaltyDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addOpponentPenalty(id, dto));
    }


    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getGameEvents(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getGameEvents(id));
    }
}