package cz.landspa.statsapp2.model.DTO.game;

import cz.landspa.statsapp2.model.entity.Game;
import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.model.entity.Roster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRosterDTO {
    Game game;

    List<Roster> roster;

    List<Player> notInRoster;
}
