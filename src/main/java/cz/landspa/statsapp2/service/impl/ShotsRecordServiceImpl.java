package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.model.DTO.shotsRecored.ShotsPeriodDTO;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.stats.ShotsRecord;
import cz.landspa.statsapp2.repository.ShotsRecordRepository;
import cz.landspa.statsapp2.service.ShotsRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShotsRecordServiceImpl implements ShotsRecordService {

    private final ShotsRecordRepository shotsRecordRepository;

    public ShotsRecordServiceImpl(ShotsRecordRepository shotsRecordRepository) {
        this.shotsRecordRepository = shotsRecordRepository;
    }

    @Override
    public ShotsRecord saveOrUpdateShots(Long gameId, Integer period, ShotsPeriodDTO input) {
        ShotsRecord record = shotsRecordRepository.findByGameIdAndPeriod(gameId, period)
                .orElseGet(() -> {
                    ShotsRecord newRecord = new ShotsRecord();
                    Game game = new Game();
                    game.setId(gameId);
                    newRecord.setGame(game);
                    newRecord.setPeriod(period);
                    return newRecord;
                });

        record.setShots(input.shots());
        return shotsRecordRepository.save(record);
    }

    @Override
    public List<ShotsRecord> getGameShots(Long gameId) {
        return shotsRecordRepository.findByGameId(gameId);
    }

    @Override
    public ShotsRecord getGamePeriodShots(Long gameId, Integer period) {
        return shotsRecordRepository.findByGameIdAndPeriod(gameId, period)
                .orElseThrow(() -> new IllegalArgumentException("Záznam pro tuto třetinu neexistuje"));
    }
}
