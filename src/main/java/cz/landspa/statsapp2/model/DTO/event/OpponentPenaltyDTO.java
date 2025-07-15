package cz.landspa.statsapp2.model.DTO.event;

public record OpponentPenaltyDTO(
        Integer time,
        Integer minutes,
        String type,
        Boolean coincidental
) implements EventDTO {}
