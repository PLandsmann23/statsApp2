package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.entity.stats.SavesRecord;
import cz.landspa.statsapp2.model.entity.stats.ShotsRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavesRecordRepository extends JpaRepository<SavesRecord, Long> {

    List<SavesRecord> findByGameId(Long gameId);

    List<SavesRecord> findByGameIdAndGoalkeeperId(Long gameId, Long goalkeeperId);

    List<SavesRecord> findByGameIdAndPeriod(Long gameId, Integer period);

    Optional<SavesRecord> getByGameIdAndGoalkeeperIdAndPeriod(Long gameId, Long goalkeeperId, Integer period);

}
