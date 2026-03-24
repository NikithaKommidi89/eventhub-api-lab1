package com.eventhub.api.service;

import com.eventhub.api.dto.EventDTO;
import com.eventhub.api.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface EventService {

    PageResponse<EventDTO> getEvents(Long categoryId, Double minPrice,
                                     Double maxPrice, LocalDateTime startDate,
                                     LocalDateTime endDate, Pageable pageable);

    EventDTO getEventById(Long id);

    EventDTO createEvent(EventDTO dto);

    EventDTO updateEvent(Long id, EventDTO dto);

    void deleteEvent(Long id);
}
