package com.showtime_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Showtime {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "movie_id", nullable = false)
	private Long movieId;
	
	@Column(name = "show_time", nullable = false)
	private LocalDateTime showTime;
	
	@Column(name = "available_seats", nullable = false)
	private Integer availableSeats;
	@ManyToOne
	@JoinColumn(name = "cinema_id")
	private Cinema cinema;

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMovieId() {
		return movieId;
	}
	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
	public LocalDateTime getShowTime() {
		return showTime;
	}
	public void setShowTime(LocalDateTime showTime) {
		this.showTime = showTime;
	}
	public Integer getAvailableSeats() {
		return availableSeats;
	}
	public void setAvailableSeats(Integer availableSeats) {
		this.availableSeats = availableSeats;
	}
}
