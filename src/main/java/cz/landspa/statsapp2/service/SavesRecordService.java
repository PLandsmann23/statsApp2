package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.DTO.savesRecord.SavesPeriodDTO;
import cz.landspa.statsapp2.model.entity.stats.SavesRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SavesRecordService {

    SavesRecord saveOrUpdateSaves(Long gameId, Integer period, SavesPeriodDTO input);


    List<SavesRecord> getGameSaves(Long gameId);


    List<SavesRecord> getPeriodSaves(Long gameId, Integer period);

    List<SavesRecord> getGameGoalieSaves(Long gameId, Long gkId);

    SavesRecord getByGameGoalkeeperPeriod(Long gameId, Long goalkeeperId, Integer period);

}
