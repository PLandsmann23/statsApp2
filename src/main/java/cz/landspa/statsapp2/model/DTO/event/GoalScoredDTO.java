package cz.landspa.statsapp2.model.DTO.event;

import java.util.List;

// GoalScoredDTO jako record implementující EventDTO
public record GoalScoredDTO(
        Integer time,
        Long scorerId,
        List<Long> assistIds,
        List<Long> onIceIds,
        String situation,
        String type  // musí být tu, aby Jackson věděl, co to je
) implements EventDTO {}
