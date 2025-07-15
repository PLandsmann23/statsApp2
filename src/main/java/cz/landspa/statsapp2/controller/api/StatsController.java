package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.DTO.stats.TeamStats;
import cz.landspa.statsapp2.model.DTO.stats.TeamStatsSum;
import cz.landspa.statsapp2.model.entity.Team;
import cz.landspa.statsapp2.service.StatsService;
import cz.landspa.statsapp2.service.TeamService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamStatsSummary(@PathVariable Long teamId, @Param("range") String range) {

        try {
            // tvůj kód
        TeamStatsSum stats = statsService.getTeamStats(teamId, range);

         return ResponseEntity.ok(stats);


        } catch (Exception e) {
            e.printStackTrace(); // 👈 to je nejdůležitější
            throw e; // nebo vrať error
        }

    }

}
