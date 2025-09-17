package cz.landspa.statsapp2.model.DTO.event;

import java.util.List;

public record GoalScoredDTO(
        Integer time,
        Long scorerId,
        List<Long> assistIds,
        List<Long> onIceIds,
        String situation,
        String type
) implements EventDTO {}
