package com.booking_service.entity;

public class LockSeatResponse {
    private String bookingId;

    // Constructors
    public LockSeatResponse() {}
    public LockSeatResponse(String bookingId) {
        this.bookingId = bookingId;
    }

    // Getter & Setter
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
