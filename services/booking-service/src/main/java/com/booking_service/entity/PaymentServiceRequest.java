package com.booking_service.entity;

public class PaymentServiceRequest {
    private String bookingId;
    private double amount;

    public PaymentServiceRequest(String bookingId, double amount) {
        this.bookingId = bookingId;
        this.amount = amount;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}