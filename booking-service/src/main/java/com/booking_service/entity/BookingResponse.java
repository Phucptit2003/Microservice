package com.booking_service.entity;

public class BookingResponse {
    private String bookingId;
    private String paymentLink;

    public BookingResponse(String bookingId, String paymentLink) {
        this.bookingId = bookingId;
        this.paymentLink = paymentLink;
    }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getPaymentLink() { return paymentLink; }
    public void setPaymentLink(String paymentLink) { this.paymentLink = paymentLink; }
}