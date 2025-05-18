package com.booking_service.entity;

import java.util.List;

public class BookSeatRequest {
    private long showtimeId;
    private List<String> seatNumbers;

    public BookSeatRequest(long showtimeId, List<String> seatNumbers) {
        this.showtimeId = showtimeId;
        this.seatNumbers = seatNumbers;
    }

    public long getShowtimeId() { return showtimeId; }
    public void setShowtimeId(long showtimeId) { this.showtimeId = showtimeId; }
    public List<String> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<String> seatNumbers) { this.seatNumbers = seatNumbers; }
}
