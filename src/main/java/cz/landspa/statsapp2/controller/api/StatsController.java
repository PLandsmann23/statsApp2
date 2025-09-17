package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.DTO.stats.TeamStatsSum;
import cz.landspa.statsapp2.service.StatsService;
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

        TeamStatsSum stats = statsService.getTeamStats(teamId, range);

         return ResponseEntity.ok(stats);



    }

}
