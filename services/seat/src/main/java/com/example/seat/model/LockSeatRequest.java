package com.example.seat.model;

import java.util.List;

public class LockSeatRequest {
    private Long showtimeId;
    private List<String> seatNumbers;

    public Long getShowtimeId() { return showtimeId; }
    public void setShowtimeId(Long showtimeId) { this.showtimeId = showtimeId; }
    public List<String> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<String> seatNumbers) { this.seatNumbers = seatNumbers; }
}
