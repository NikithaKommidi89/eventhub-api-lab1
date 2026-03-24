package com.eventhub.api.repository;


import com.eventhub.api.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository
    extends CrudRepository<Event, Long> {

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
            Pageable pageable
    );
}

