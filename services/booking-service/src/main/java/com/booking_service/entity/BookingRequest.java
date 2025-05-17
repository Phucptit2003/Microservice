package com.booking_service.entity;

import java.util.Collections;
import java.util.List;

public  class BookingRequest {
    private String userEmail;
    private String showtimeId;
    private String seats;

    public BookingRequest(String userEmail, String showtimeId, String seats) {
        this.userEmail = userEmail;
        this.showtimeId = showtimeId;
        this.seats = seats;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public List<String> getSeats() {
        return Collections.singletonList(seats);
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
}