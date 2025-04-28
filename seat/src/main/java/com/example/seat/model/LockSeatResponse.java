package com.example.seat.model;

public class LockSeatResponse {
    private String bookingId;

    public LockSeatResponse(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}