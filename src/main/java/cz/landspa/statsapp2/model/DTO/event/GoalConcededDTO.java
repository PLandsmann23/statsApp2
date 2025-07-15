package cz.landspa.statsapp2.model.DTO.event;

import java.util.List;

public record GoalConcededDTO(
        Integer time,
        List<Long> onIceIds,

        Long inGoalId,
        String situation,
        String type
) implements EventDTO {}
