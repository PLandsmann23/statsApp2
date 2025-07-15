package cz.landspa.statsapp2.model.DTO.team;

import cz.landspa.statsapp2.model.DTO.game.GameWithScoreDTO;
import cz.landspa.statsapp2.model.entity.Player;
import cz.landspa.statsapp2.model.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDetailDTO {
    private Team team;
    private List<Player> players;
    private List<GameWithScoreDTO> games;
}
