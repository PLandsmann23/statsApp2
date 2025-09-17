package cz.landspa.statsapp2.exception;

import cz.landspa.statsapp2.model.entity.Roster;
import lombok.Getter;

import java.util.List;

@Getter
public class MissingActiveGoalkeeperException extends RuntimeException {
    private final List<Roster> goalkeepers;

    public MissingActiveGoalkeeperException(List<Roster> goalkeepers) {
        super("Chybí označení chytajícího brankáře");
        this.goalkeepers = goalkeepers;
    }

}
