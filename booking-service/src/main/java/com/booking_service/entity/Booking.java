package com.booking_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private Long showtimeId;
	private String seats; // Lưu dạng "A1,A2"
	private String status; // PENDING, CONFIRMED, CANCELLED
	private String movieName;
	private String showtime;
	private String userEmail;
	private LocalDateTime createdAt;

	// Getters và Setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
	public Long getShowtimeId() { return showtimeId; }
	public void setShowtimeId(Long showtimeId) { this.showtimeId = showtimeId; }
	public String getSeats() { return seats; }
	public void setSeats(String seats) { this.seats = seats; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public String getMovieName() { return movieName; }
	public void setMovieName(String movieName) { this.movieName = movieName; }
	public String getShowtime() { return showtime; }
	public void setShowtime(String showtime) { this.showtime = showtime; }
	public String getUserEmail() { return userEmail; }
	public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}