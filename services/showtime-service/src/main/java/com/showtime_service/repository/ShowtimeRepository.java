package com.showtime_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.showtime_service.entity.Showtime;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovieId(Long movieId);

}
