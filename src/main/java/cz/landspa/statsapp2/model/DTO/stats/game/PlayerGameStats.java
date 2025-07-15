package cz.landspa.statsapp2.model.DTO.stats.game;

import cz.landspa.statsapp2.model.entity.Roster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerGameStats {
    private Roster player;
    private Long goals;
    private Long assists;
    private Long plus;
    private Long minus;
    private Long penaltyMinutes;


}
