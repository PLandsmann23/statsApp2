package cz.landspa.statsapp2.exception;

import cz.landspa.statsapp2.model.DTO.roster.RosterConflictDTO;

public class UniqueKeyConstraintViolationException extends RuntimeException{
    public UniqueKeyConstraintViolationException(){
        super();
    }
}
