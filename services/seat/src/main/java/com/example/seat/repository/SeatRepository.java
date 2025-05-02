package com.example.seat.repository;

import com.example.seat.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowtimeId(Long showtimeId);

    List<Seat> findByShowtimeIdAndStatus(Long showtimeId, String status);

    @Query("SELECT s FROM Seat s WHERE s.showtimeId = :showtimeId AND s.status = :status AND (s.locked = false OR s.lockedUntil < :currentTime)")
    List<Seat> findAvailableSeatsForLocking(@Param("showtimeId") Long showtimeId,
                                            @Param("status") String status,
                                            @Param("currentTime") LocalDateTime currentTime);
    List<Seat> findByShowtimeIdAndSeatNumberIn(Long showtimeId, List<String> seatNumbers);

}