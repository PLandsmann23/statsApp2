package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.model.DTO.savesRecord.SavesPeriodDTO;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Roster;
import cz.landspa.statsapp2.model.entity.stats.SavesRecord;
import cz.landspa.statsapp2.repository.SavesRecordRepository;
import cz.landspa.statsapp2.service.SavesRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SavesRecordServiceImpl implements SavesRecordService {

    private final SavesRecordRepository savesRecordRepository;

    public SavesRecordServiceImpl(SavesRecordRepository savesRecordRepository) {
        this.savesRecordRepository = savesRecordRepository;
    }

    @Override
    public SavesRecord saveOrUpdateSaves(Long gameId, Integer period, SavesPeriodDTO input) {
        Optional<SavesRecord> existing = savesRecordRepository.getByGameIdAndGoalkeeperIdAndPeriod(gameId, input.goalkeeper(), period);

        SavesRecord record = existing.orElseGet(() -> {
            SavesRecord newRecord = new SavesRecord();
            newRecord.setGame(new Game());
            newRecord.getGame().setId(gameId);
            newRecord.setGoalkeeper(new Roster());
            newRecord.getGoalkeeper().setId(input.goalkeeper());
            newRecord.setPeriod(period);
            return newRecord;
        });

        record.setSaves(input.saves());
        return savesRecordRepository.save(record);
    }

    @Override
    public List<SavesRecord> getGameSaves(Long gameId) {
        return savesRecordRepository.findByGameId(gameId);
    }

    @Override
    public List<SavesRecord> getPeriodSaves(Long gameId, Integer period) {
        return savesRecordRepository.findByGameIdAndPeriod(gameId,period);
    }

    @Override
    public List<SavesRecord> getGameGoalieSaves(Long gameId, Long goalkeeperId) {
        return savesRecordRepository.findByGameIdAndGoalkeeperId(gameId, goalkeeperId);
    }

    @Override
    public SavesRecord getByGameGoalkeeperPeriod(Long gameId, Long goalkeeperId, Integer period) {
        return savesRecordRepository.getByGameIdAndGoalkeeperIdAndPeriod(gameId, goalkeeperId, period).orElseThrow(() -> new IllegalArgumentException("Záznam pro tuto třetinu neexistuje"));
    }
}
