package com.booking_service.entity;

public class PaymentResponse {
    private String sessionId;

    public PaymentResponse(String sessionId) { this.sessionId = sessionId; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}