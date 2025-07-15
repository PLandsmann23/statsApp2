package cz.landspa.statsapp2.model.DTO.game;


import cz.landspa.statsapp2.model.DTO.event.PeriodEvents;
import cz.landspa.statsapp2.model.DTO.stats.game.PeriodGoals;
import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Roster;
import cz.landspa.statsapp2.model.entity.event.Event;
import cz.landspa.statsapp2.model.entity.stats.SavesRecord;
import cz.landspa.statsapp2.model.entity.stats.ShotsRecord;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameFullInfoDTO {

    Game game;

    List<Roster> roster;

    List<PeriodEvents> events;

    List<ShotsRecord> shots;

    List<SavesRecord> savesRecords;

    List<PeriodGoals> periodGoals;

    Long goalsScored;

    Long goalsConceded;
}
