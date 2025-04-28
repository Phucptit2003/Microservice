package com.example.seat.service;

import com.example.seat.model.Seat;
import com.example.seat.model.SeatResponse;
import com.example.seat.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;
    private static final Logger log = LoggerFactory.getLogger(SeatService.class);

    public List<SeatResponse> getSeatsByShowtime(Long showtimeId) {
        List<Seat> seats = seatRepository.findByShowtimeId(showtimeId);
        return seats.stream()
                .map(seat -> new SeatResponse(seat.getSeatNumber(), seat.getStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    public String lockSeats(Long showtimeId, List<String> seatNumbers) {
        log.info("Đang cố khóa ghế cho showtimeId: {}, seatNumbers: {}", showtimeId, seatNumbers);
        List<Seat> seats = seatRepository.findByShowtimeIdAndSeatNumberIn(showtimeId, seatNumbers);
        if (seats.size() != seatNumbers.size()) {
            log.warn("Một số ghế không tồn tại cho showtimeId: {}. Tìm thấy: {}, Yêu cầu: {}",
                    showtimeId, seats.stream().map(Seat::getSeatNumber).collect(Collectors.toList()), seatNumbers);
            return null;
        }
        for (Seat seat : seats) {
            if (!seat.getStatus().equals("AVAILABLE")) {
                log.warn("Ghế {} không khả dụng cho showtimeId: {}. Trạng thái: {}",
                        seat.getSeatNumber(), showtimeId, seat.getStatus());
                return null;
            }
        }
        String bookingId = UUID.randomUUID().toString();
        for (Seat seat : seats) {
            seat.setStatus("LOCKED");
            seat.setBookingId(bookingId); // Giả sử Seat có trường bookingId
            seatRepository.save(seat);
        }
        log.info("Khóa ghế thành công cho showtimeId: {}, bookingId: {}", showtimeId, bookingId);
        return bookingId;
    }

    public List<String> getUnavailableSeats(Long showtimeId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.findByShowtimeIdAndSeatNumberIn(showtimeId, seatNumbers);
        List<String> requestedSeats = new ArrayList<>(seatNumbers);
        List<String> availableSeats = seats.stream()
                .filter(seat -> seat.getStatus().equals("AVAILABLE"))
                .map(Seat::getSeatNumber)
                .collect(Collectors.toList());
        requestedSeats.removeAll(availableSeats);
        return requestedSeats;
    }

    @Transactional
    public void bookSeats(Long showtimeId, List<String> seatNumbers) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Seat> seats = seatRepository.findByShowtimeId(showtimeId);
        for (String seatNumber : seatNumbers) {
            Seat seat = seats.stream()
                    .filter(s -> s.getSeatNumber().equals(seatNumber))
                    .findFirst()
                    .orElse(null);
            if (seat != null) {
                // Ensure the seat is locked and the lock is still valid
                if (seat.isLocked() && seat.getLockedUntil().isAfter(currentTime)) {
                    seat.setStatus("BOOKED");
                    seat.setLocked(false);
                    seat.setLockedUntil(null);
                    seatRepository.save(seat);
                } else {
                    throw new IllegalStateException("Seat " + seatNumber + " is not locked or lock has expired");
                }
            }
        }
    }

    @Transactional
    public void unlockSeats(Long showtimeId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.findByShowtimeId(showtimeId);
        for (String seatNumber : seatNumbers) {
            Seat seat = seats.stream()
                    .filter(s -> s.getSeatNumber().equals(seatNumber))
                    .findFirst()
                    .orElse(null);
            if (seat != null && seat.getStatus().equals("LOCKED")) {
                seat.setStatus("AVAILABLE");
                seat.setLocked(false);
                seat.setLockedUntil(null);
                seatRepository.save(seat);
            }
        }
    }
}