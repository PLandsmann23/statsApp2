package cz.landspa.statsapp2.controller.api;

import cz.landspa.statsapp2.model.DTO.event.EventDTO;
import cz.landspa.statsapp2.model.entity.event.Event;
import cz.landspa.statsapp2.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody EventDTO dto) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, dto));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        Event event = eventService.getById(eventId).orElseThrow(()->new IllegalArgumentException("Událost nebyla nalezena"));
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(Map.of("message", "Event smazán"));
    }
}
