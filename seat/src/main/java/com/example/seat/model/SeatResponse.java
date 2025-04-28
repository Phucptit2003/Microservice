package com.example.seat.model;

public class SeatResponse {
    private String seatNumber;
    private String status;

    public SeatResponse(String seatNumber, String status) {
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
