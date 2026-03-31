package com.eventhub.api.repository;

import com.eventhub.api.model.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Find registrations by user
    List<Registration> findByUserId(Long userId);

    // Find registrations by user with pagination
    Page<Registration> findByUserId(Long userId, Pageable pageable);

    // Find registrations by status
    List<Registration> findByStatus(Registration.RegistrationStatus status);

    // Find registrations by date range
    List<Registration> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end);

    // Join query: registrations with their items and events
    @Query("""
                SELECT DISTINCT r FROM Registration r
                JOIN FETCH r.user
                JOIN FETCH r.items i
                JOIN FETCH i.event
                WHERE r.user.id = :userId
            """)
    List<Registration> findByUserIdWithItems(@Param("userId") Long userId);

    // Aggregate: total revenue per user
    @Query("""
                SELECT r.user.name, SUM(r.totalAmount)
                FROM Registration r
                WHERE r.status = 'CONFIRMED'
                GROUP BY r.user.id, r.user.name
                ORDER BY SUM(r.totalAmount) DESC
            """)
    List<Object[]> findTotalRevenuePerUser();
}
