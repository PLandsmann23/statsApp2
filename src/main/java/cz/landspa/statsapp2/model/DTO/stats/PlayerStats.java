package cz.landspa.statsapp2.model.DTO.stats;

import cz.landspa.statsapp2.model.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStats {
    private Player player;
    private Long gamesPlayed = 0L;
    private Long goals = 0L;
    private Long assists = 0L;
    private Long plus = 0L;
    private Long minus = 0L;
    private Long penaltyMinutes = 0L;



}
