package com.eventhub.api.controller;

import com.eventhub.api.dto.EventDTO;
import com.eventhub.api.dto.PageResponse;
import com.eventhub.api.service.EventServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Events", description = "Event management endpoints")
public class EventController {

    private final EventServiceImpl eventService;

    public EventController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    // GET /api/v1/events
    @GetMapping
    @Operation(summary = "List events with pagination, filtering and sorting")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved events")
    public ResponseEntity<PageResponse<EventDTO>> getEvents(
            @Parameter(description = "Category ID filter")
            @RequestParam(required = false) Long category,

            @Parameter(description = "Minimum price filter")
            @RequestParam(required = false) Double minPrice,

            @Parameter(description = "Maximum price filter")
            @RequestParam(required = false) Double maxPrice,

            @Parameter(description = "Start date filter (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "End date filter (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Sort field and direction e.g. title,asc or ticketPrice,desc")
            @RequestParam(defaultValue = "id,asc") String sort) {

        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        Sort.Direction direction = sortParts.length > 1
                && sortParts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(
                eventService.getEvents(category, minPrice, maxPrice,
                        startDate, endDate, pageable));
    }

    // GET /api/v1/events/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    // POST /api/v1/events
    @PostMapping
    @Operation(summary = "Create a new event")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.createEvent(dto));
    }

    // PUT /api/v1/events/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing event")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event updated"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventDTO dto) {
        return ResponseEntity.ok(eventService.updateEvent(id, dto));
    }

    // DELETE /api/v1/events/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Event deleted"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}