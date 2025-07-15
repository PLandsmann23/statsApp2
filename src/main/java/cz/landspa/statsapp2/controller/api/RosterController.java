package cz.landspa.statsapp2.controller.api;

import cz.landspa.statsapp2.model.entity.Roster;
import cz.landspa.statsapp2.service.RosterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/roster")
public class RosterController {
    private final RosterService rosterService;

    public RosterController(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Roster> updateRoster(@PathVariable Long id, @Valid @RequestBody Roster roster) {
        return ResponseEntity.ok(rosterService.updateRoster(roster, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoster(@PathVariable Long id) {
        rosterService.deleteRoster(id);
        return ResponseEntity.ok(Map.of("message", "Hráč byl odebrán ze soupisky"));
    }
}
