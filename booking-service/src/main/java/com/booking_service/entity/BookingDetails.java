package com.booking_service.entity;

import java.util.List;

public class BookingDetails {
    private String userEmail;
    private String movieName;
    private String showtime;
    private long showtimeId;
    private List<String> seats;
    private String userName;
    private String createdAt;

    public BookingDetails(String userEmail, String movieName, String showtime, long showtimeId, List<String> seats, String userName, String createdAt) {
        this.userEmail = userEmail;
        this.movieName = movieName;
        this.showtime = showtime;
        this.showtimeId = showtimeId;
        this.seats = seats;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
    public String getShowtime() { return showtime; }
    public void setShowtime(String showtime) { this.showtime = showtime; }
    public long getShowtimeId() { return showtimeId; }
    public void setShowtimeId(long showtimeId) { this.showtimeId = showtimeId; }
    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}