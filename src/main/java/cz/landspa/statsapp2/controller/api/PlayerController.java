package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @Valid @RequestBody Player player) {
        Player updated = playerService.updatePlayer(player, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok(Map.of("message", "Hráč byl úspěšně smazán"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        return ResponseEntity.ok(player);
    }


}
