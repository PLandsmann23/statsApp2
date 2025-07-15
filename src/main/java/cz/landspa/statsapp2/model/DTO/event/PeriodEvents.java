package cz.landspa.statsapp2.model.DTO.event;

import cz.landspa.statsapp2.model.entity.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PeriodEvents {
    Long period;

    List<Event> events;
}
