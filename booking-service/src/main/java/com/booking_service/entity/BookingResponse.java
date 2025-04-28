package com.booking_service.entity;

public class BookingResponse {
    private String bookingId;

    public BookingResponse(String bookingId) { this.bookingId = bookingId; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
}