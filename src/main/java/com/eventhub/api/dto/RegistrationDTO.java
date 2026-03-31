package com.eventhub.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Registration Data Transfer Object")
public class RegistrationDTO {

    @Schema(description = "Registration ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "User ID is required")
    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "User name", accessMode = Schema.AccessMode.READ_ONLY)
    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Registration date", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime registrationDate;

    @Schema(description = "Status: PENDING, CONFIRMED, CANCELLED", example = "PENDING")
    private String status;

    @Schema(description = "Total amount", accessMode = Schema.AccessMode.READ_ONLY)
    private Double totalAmount;

    @Schema(description = "Registration line items")
    private List<RegistrationItemDTO> items;

    public RegistrationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<RegistrationItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RegistrationItemDTO> items) {
        this.items = items;
    }
}
