package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.entity.stats.ShotsRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShotsRecordRepository extends JpaRepository<ShotsRecord, Long> {

    Optional<ShotsRecord> findByGameIdAndPeriod(Long gameId, Integer period);
    List<ShotsRecord> findByGameId(Long gameId);
}
