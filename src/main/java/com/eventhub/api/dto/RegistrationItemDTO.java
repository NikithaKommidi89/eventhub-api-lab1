package com.eventhub.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Registration Item DTO")
public class RegistrationItemDTO {

    @Schema(description = "Item ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Event ID is required")
    @Schema(description = "Event ID", example = "1")
    private Long eventId;

    @Schema(description = "Event title", accessMode = Schema.AccessMode.READ_ONLY)
    private String eventTitle;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Number of tickets", example = "2")
    private Integer quantity;

    @Schema(description = "Price per ticket", accessMode = Schema.AccessMode.READ_ONLY)
    private Double unitPrice;

    @Schema(description = "Line subtotal", accessMode = Schema.AccessMode.READ_ONLY)
    private Double subtotal;

    public RegistrationItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
