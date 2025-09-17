package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.DTO.shotsRecored.ShotsPeriodDTO;
import cz.landspa.statsapp2.model.entity.stats.ShotsRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShotsRecordService {
    ShotsRecord saveOrUpdateShots(Long gameId, Integer period, ShotsPeriodDTO input);

    List<ShotsRecord> getGameShots(Long gameId);

    ShotsRecord getGamePeriodShots(Long gameId, Integer period);

}
