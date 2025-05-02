package com.booking_service.entity;

import java.util.List;

public class LockSeatRequest {
    private String showtimeId;
    private List<String> seatNumbers;

    public LockSeatRequest(String showtimeId, List<String> seatNumbers) {
        this.showtimeId = showtimeId;
        this.seatNumbers = seatNumbers;
    }

    public String getShowtimeId() { return showtimeId; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }
    public List<String> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<String> seatNumbers) { this.seatNumbers = seatNumbers; }
}
