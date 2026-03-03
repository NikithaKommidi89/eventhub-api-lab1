package com.eventhub.api.controller;

import com.eventhub.api.dto.EventDTO;
import com.eventhub.api.model.Event;
import com.eventhub.api.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // GET /api/events
    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents()
                .stream()
                .map(event -> new EventDTO(
                        event.getId(),
                        event.getName(),
                        event.getTicketPrice(),
                        event.getCategory(),
                        event.getEventDate()
                ))
                .toList();
    }

    // GET /api/events/{id}
    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);

        if (event == null) {
            return null;
        }

        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getTicketPrice(),
                event.getCategory(),
                event.getEventDate()
        );
    }

    // POST /api/events
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    // PUT /api/events/{id}
    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id,
                             @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    // DELETE /api/events/{id}
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
