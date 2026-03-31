package com.eventhub.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    private Double totalAmount;

    @OneToMany(mappedBy = "registration",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<RegistrationItem> items;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }

    public enum RegistrationStatus {
        PENDING, CONFIRMED, CANCELLED
    }

    public Registration() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime d) { this.registrationDate = d; }

    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public List<RegistrationItem> getItems() { return items; }
    public void setItems(List<RegistrationItem> items) { this.items = items; }
}