package cz.landspa.statsapp2.model.DTO.game;


import cz.landspa.statsapp2.model.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameWithScoreDTO {

    Game game;

    Long goalsScored;

    Long goalsConceded;
}
