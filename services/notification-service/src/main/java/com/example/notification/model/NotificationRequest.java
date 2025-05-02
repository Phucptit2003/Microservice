package com.example.notification.model;


public class NotificationRequest {
    private String email;
    private String bookingId;
    private String movieName;
    private String showtime;
    private String seats;

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
    public String getShowtime() { return showtime; }
    public void setShowtime(String showtime) { this.showtime = showtime; }
    public String getSeats() { return seats; }
    public void setSeats(String seats) { this.seats = seats; }
}