package com.eventhub.api.repository;

import com.eventhub.api.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // --- Derived query methods ---

    // Find events by category
    List<Event> findByCategoryId(Long categoryId);

    // Find events by price range
    List<Event> findByTicketPriceBetween(Double minPrice, Double maxPrice);

    // Find active events
    List<Event> findByIsActiveTrue();

    // Find events by category with pagination
    Page<Event> findByCategoryId(Long categoryId, Pageable pageable);

    // --- Custom @Query methods ---

    // Complex filtered search with pagination
    @Query("""
                 SELECT e FROM Event e
                WHERE (:categoryId IS NULL OR e.category.id = :categoryId)
                  AND (:minPrice IS NULL OR e.ticketPrice >= :minPrice)
                  AND (:maxPrice IS NULL OR e.ticketPrice <= :maxPrice)
                  AND (:startDate IS NULL OR e.eventDate >= :startDate)
                  AND (:endDate IS NULL OR e.eventDate <= :endDate)
            """)
    Page<Event> findWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // Total number of registrations per event
    @Query("""
                SELECT e.title, COUNT(ri) AS totalRegistrations
                FROM RegistrationItem ri
                JOIN ri.event e
                GROUP BY e.id, e.title
                ORDER BY totalRegistrations DESC
            """)
    List<Object[]> findTotalRegistrationsPerEvent();

    // Average ticket revenue per category
    @Query("""
                SELECT c.name, AVG(e.ticketPrice) AS avgPrice
                FROM Event e
                JOIN e.category c
                WHERE e.isActive = true
                GROUP BY c.id, c.name
            """)
    List<Object[]> findAveragePricePerCategory();

    // Search events by title keyword (case-insensitive)
    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Event> searchByTitle(@Param("keyword") String keyword);

    // Find upcoming active events after a given date
    @Query("SELECT e FROM Event e WHERE e.isActive = true AND e.eventDate >= :from ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEvents(@Param("from") LocalDateTime from);
}

