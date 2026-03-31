package com.eventhub.api.repository;

import com.eventhub.api.model.RegistrationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationItemRepository extends JpaRepository<RegistrationItem, Long> {

    // Find items by registration
    List<RegistrationItem> findByRegistrationId(Long registrationId);

    // Find items by event
    List<RegistrationItem> findByEventId(Long eventId);

    // Aggregate: total tickets sold per event
    @Query("""
                SELECT ri.event.title, SUM(ri.quantity) AS totalTickets
                FROM RegistrationItem ri
                GROUP BY ri.event.id, ri.event.title
                ORDER BY totalTickets DESC
            """)
    List<Object[]> findTotalTicketsSoldPerEvent();
}
