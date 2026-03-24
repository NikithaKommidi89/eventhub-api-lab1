package com.eventhub.api.service;

import com.eventhub.api.dto.EventDTO;
import com.eventhub.api.dto.PageResponse;
import com.eventhub.api.exception.EventNotFoundException;
import com.eventhub.api.model.Category;
import com.eventhub.api.model.Event;
import com.eventhub.api.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventServiceImpl implements   EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final CategoryService categoryService;

    public EventServiceImpl(EventRepository eventRepository,
                            CategoryService categoryService) {
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
    }

    @Cacheable(value = "events", key = "{#categoryId, #minPrice, #maxPrice, #startDate, #endDate, #pageable}")
    public PageResponse<EventDTO> getEvents(
            Long categoryId, Double minPrice, Double maxPrice,
            LocalDateTime startDate, LocalDateTime endDate,
            Pageable pageable) {

        log.info("Fetching events with filters from DB");
        Page<Event> page = eventRepository.findWithFilters(
                categoryId, minPrice, maxPrice, startDate, endDate, pageable);

        return new PageResponse<>(
                page.getContent().stream().map(this::toDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst());
    }

    @Cacheable(value = "event", key = "#id")
    public EventDTO getEventById(Long id) {
        log.info("Fetching event id={}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return toDTO(event);
    }

    @CacheEvict(value = { "events", "event" }, allEntries = true)
    public EventDTO createEvent(EventDTO dto) {
        Category category = categoryService.getCategoryEntity(dto.getCategoryId());
        Event event = fromDTO(dto, new Event(), category);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        Event saved = eventRepository.save(event);
        log.info("Created event id={}", saved.getId());
        return toDTO(saved);
    }

    @CacheEvict(value = { "events", "event" }, allEntries = true)
    public EventDTO updateEvent(Long id, EventDTO dto) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        Category category = categoryService.getCategoryEntity(dto.getCategoryId());
        fromDTO(dto, existing, category);
        existing.setUpdatedAt(LocalDateTime.now());
        return toDTO(eventRepository.save(existing));
    }

    @CacheEvict(value = { "events", "event" }, allEntries = true)
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException(id);
        }
        eventRepository.deleteById(id);
        log.info("Deleted event id={}", id);
    }

    private Event fromDTO(EventDTO dto, Event event, Category category) {
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setTicketPrice(dto.getTicketPrice());
        event.setCategory(category);
        event.setActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        event.setEventDate(dto.getEventDate());
        return event;
    }

    private EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setTicketPrice(event.getTicketPrice());
        if (event.getCategory() != null) {
            dto.setCategoryId(event.getCategory().getId());
            dto.setCategoryName(event.getCategory().getName());
        }
        dto.setIsActive(event.getActive());
        dto.setEventDate(event.getEventDate());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());
        return dto;
    }
}