package cz.landspa.statsapp2.exception;

import cz.landspa.statsapp2.model.DTO.roster.RosterConflictDTO;
import lombok.Getter;

@Getter
public class RosterConflictException extends RuntimeException {
    private final RosterConflictDTO conflict;

    public RosterConflictException(RosterConflictDTO conflict) {
        super("Konflikt čísel na soupisce");
        this.conflict = conflict;
    }

}
