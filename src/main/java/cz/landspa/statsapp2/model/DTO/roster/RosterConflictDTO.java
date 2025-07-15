package cz.landspa.statsapp2.model.DTO.roster;


import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.model.entity.Roster;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RosterConflictDTO {
    private Player newPlayer;
    private Roster editedRoster;
    private Roster conflictingRoster;
    private Integer conflictingNumber;
}
