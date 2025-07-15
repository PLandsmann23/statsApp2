package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.DTO.savesRecord.SavesPeriodDTO;
import cz.landspa.statsapp2.model.DTO.shotsRecored.ShotsPeriodDTO;
import cz.landspa.statsapp2.model.DTO.stats.game.GamePlayerStatsSum;
import cz.landspa.statsapp2.model.entity.stats.SavesRecord;
import cz.landspa.statsapp2.model.entity.stats.ShotsRecord;
import cz.landspa.statsapp2.service.SavesRecordService;
import cz.landspa.statsapp2.service.ShotsRecordService;
import cz.landspa.statsapp2.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games/{gameId}")
public class GameStatsController {

    private final ShotsRecordService shotsRecordService;
    private final SavesRecordService savesRecordService;
    private final StatsService statsService;

    public GameStatsController(ShotsRecordService shotsRecordService, SavesRecordService savesRecordService, StatsService statsService) {
        this.shotsRecordService = shotsRecordService;
        this.savesRecordService = savesRecordService;
        this.statsService = statsService;
    }

    @GetMapping("/shots/{period}")
    public ResponseEntity<ShotsRecord> getShotsForPeriod(@PathVariable Long gameId, @PathVariable Integer period) {
        return ResponseEntity.ok(shotsRecordService.getGamePeriodShots(gameId, period));
    }

    @PutMapping("/shots/{period}")
    public ResponseEntity<ShotsRecord> updateShotsForPeriod(@PathVariable Long gameId,
                                                  @PathVariable Integer period,
                                                  @RequestBody ShotsPeriodDTO input) {
        ShotsRecord shotsRecord = shotsRecordService.saveOrUpdateShots(gameId, period, input);
        return ResponseEntity.ok(shotsRecord);
    }


    @GetMapping("/saves/{period}")
    public ResponseEntity<List<SavesRecord>> getSavesForPeriod(@PathVariable Long gameId, @PathVariable Integer period) {
        return ResponseEntity.ok(savesRecordService.getPeriodSaves(gameId, period));
    }

    @PutMapping("/saves/{period}")
    public ResponseEntity<SavesRecord> updateSavesForPeriod(@PathVariable Long gameId,
                                                  @PathVariable Integer period,
                                                  @RequestBody SavesPeriodDTO input) {
       SavesRecord savesRecord =  savesRecordService.saveOrUpdateSaves(gameId, period, input);
        return ResponseEntity.ok(savesRecord);
    }

    @GetMapping("/playerStats")
    public ResponseEntity<GamePlayerStatsSum> getGamePlayerStats(@PathVariable Long gameId) {
        return ResponseEntity.ok(statsService.getGamePlayerStats(gameId));
    }

  /*  @GetMapping("/stats")
    public ResponseEntity<GameStats> getGameStats(@PathVariable Long gameId) {
        return ResponseEntity.ok(statsService.getGameStats(gameId));
    }

   */
}
