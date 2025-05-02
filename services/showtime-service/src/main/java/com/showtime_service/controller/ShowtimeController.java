package com.showtime_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.showtime_service.dtos.ShowtimeDTO;
import com.showtime_service.entity.Showtime;
import com.showtime_service.service.ShowtimeService;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {
	@Autowired
    private ShowtimeService showtimeService;
	
	@GetMapping
	public List<Showtime> fetchAllShowtimes(){
		return showtimeService.getAllShowtimes();
	}

    @GetMapping("/{id}")
    public ShowtimeDTO getShowtimeWithBookings(@PathVariable Long id) {
        return showtimeService.getShowtimeWithBookings(id);
    }
    
    @PutMapping("/{id}/book-seats/{seats}")
    public void bookSeats(@PathVariable("id") Long showtimeId, @PathVariable("seats") Integer seats) {
    	showtimeService.bookSeats(showtimeId, seats);
    }
    
    @GetMapping("/{id}/exists")
    public Boolean validateShowtime(@PathVariable("id") Long id) {
    	return showtimeService.doesShowtimeExist(id);
    }
    @GetMapping(params = "movieId")
    public List<Showtime> fetchShowtimesByMovieId(@RequestParam Long movieId) {
        return showtimeService.getShowtimesByMovieId(movieId);
    }

}
