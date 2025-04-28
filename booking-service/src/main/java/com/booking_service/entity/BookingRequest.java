package com.booking_service.entity;

import java.util.Collections;
import java.util.List;

public  class BookingRequest {
    private String userId;
    private String showtimeId;
    private String seats;

    public BookingRequest(String userId, String showtimeId, String seats) {
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.seats = seats;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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