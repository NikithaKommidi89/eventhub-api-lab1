package com.eventhub.api.dto;

import java.time.LocalDateTime;

public class EventDTO {

    private Long id;
    private String name;
    private Double ticketPrice;
    private String category;
    private LocalDateTime eventDate;

    public EventDTO(Long id, String name, Double ticketPrice,
                    String category, LocalDateTime eventDate) {
        this.id = id;
        this.name = name;
        this.ticketPrice = ticketPrice;
        this.category = category;
        this.eventDate = eventDate;
    }

    public EventDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }
}