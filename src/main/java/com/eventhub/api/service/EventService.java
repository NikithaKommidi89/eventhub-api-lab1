package com.eventhub.api.service;

import com.eventhub.api.model.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EventService {

    private final Map<Long, Event> eventStore = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public List<Event> getAllEvents() {
        return new ArrayList<>(eventStore.values());
    }

    public Event getEventById(Long id) {
        return eventStore.get(id);
    }

    public Event createEvent(Event event) {
        Long id = idGenerator.incrementAndGet();
        event.setId(id);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        eventStore.put(id, event);
        return event;
    }

    public Event updateEvent(Long id, Event event) {
        Event existing = eventStore.get(id);
        if (existing == null) {
            return null;
        }

        event.setId(id);
        event.setCreatedAt(existing.getCreatedAt());
        event.setUpdatedAt(LocalDateTime.now());
        eventStore.put(id, event);
        return event;
    }

    public void deleteEvent(Long id) {
        eventStore.remove(id);
    }
}
