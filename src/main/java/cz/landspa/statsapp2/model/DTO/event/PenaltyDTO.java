package cz.landspa.statsapp2.model.DTO.event;

public record PenaltyDTO(
        Integer time,
        Long playerId,
        Integer minutes,
        String reason,
        String type,
        Boolean coincidental
) implements EventDTO {}
