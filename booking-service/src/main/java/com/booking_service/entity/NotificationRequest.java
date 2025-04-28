package com.booking_service.entity;

public class NotificationRequest {
    private String email;
    private String bookingId;
    private String movieName;
    private String showtime;
    private String seats;

    public NotificationRequest(String email, String bookingId, String movieName, String showtime, String seats) {
        this.email = email;
        this.bookingId = bookingId;
        this.movieName = movieName;
        this.showtime = showtime;
        this.seats = seats;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
}