package cz.landspa.statsapp2.model.DTO.roster;

import lombok.Data;

@Data
public class RosterNumberUpdateDTO {
    private Long rosterId;
    private Long playerId;
    private Integer newNumber;
}
