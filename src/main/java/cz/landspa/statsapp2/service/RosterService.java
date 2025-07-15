package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.DTO.game.GameRosterDTO;
import cz.landspa.statsapp2.model.entity.Roster;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RosterService {
    Roster addPlayerToRoster(Roster roster);

    List<Roster> getRosterForGame(Long gameId);

    GameRosterDTO getRosterWithInfoForGame(Long gameId);

    void deleteRoster(Long id);

    Roster updateRoster(Roster roster, Long id);

    void changeActiveGoalkeeper(Long gameId);

    Optional<Roster> getRosterById(Long id);

}
