package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RosterRepository extends JpaRepository<Roster, Long> {
    boolean existsByGameIdAndPlayerId(Long gameId, Long playerId);



    Optional<Roster> findByGameIdAndGameNumber(Long gameId, Integer gameNumber);

    List<Roster> findAllByGameIdOrderByPlayerPositionSortOrderAscLineAsc(Long gameId);

    List<Roster> findAllByGameIdAndPlayerPositionCode(Long gameId, String code);


    Optional<Roster> findByGameIdAndPlayerPositionCodeAndActiveGk(Long gameId, String positionCode, boolean activeGK);
    List<Roster> findAllByGameIdAndPlayerPositionCodeAndActiveGk(Long gameId, String positionCode, boolean activeGK);

    Long countByGameIdAndPlayerPositionCode(Long gameId, String positionCode);
}
