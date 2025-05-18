package com.booking_service.service;

import com.booking_service.entity.*;
import com.booking_service.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

	private static final Logger log = LoggerFactory.getLogger(BookingService.class);

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private WebClient.Builder webClientBuilder;

	public String createBooking(UserResponse user, String showtimeId, List<String> seats, String movieName, String showtimeTime) {
		try {
			log.info("Tạo booking cho userId: {}, showtimeId: {}, seats: {}", user.getId(), showtimeId, seats);

			Booking booking = new Booking();
			booking.setUserId(user.getId());
			booking.setShowtimeId(Long.parseLong(showtimeId));
			booking.setSeats(String.join(",", seats));
			booking.setStatus("PENDING");
			booking.setUserEmail(user.getEmail());
			booking.setMovieName(movieName);
			booking.setShowtime(showtimeTime);
			booking.setCreatedAt(LocalDateTime.now());

			booking = bookingRepository.save(booking);
			log.info("Tạo booking thành công, bookingId: {}", booking.getId());
			return booking.getId().toString();
		} catch (Exception e) {
			log.error("Lỗi khi tạo booking: {}", e.getMessage());
			throw new RuntimeException("Lỗi khi tạo booking: " + e.getMessage());
		}
	}

	public BookingDetails confirmBooking(String bookingId) {
		try {
			log.info("Xác nhận booking cho bookingId: {}", bookingId);

			Optional<Booking> optionalBooking = bookingRepository.findById(Long.parseLong(bookingId));
			if (!optionalBooking.isPresent()) {
				log.warn("Không tìm thấy booking với bookingId: {}", bookingId);
				throw new RuntimeException("Không tìm thấy booking");
			}

			Booking booking = optionalBooking.get();
			if (!"PENDING".equals(booking.getStatus())) {
				log.warn("Booking không ở trạng thái PENDING: {}", booking.getStatus());
				throw new RuntimeException("Booking không hợp lệ để xác nhận");
			}

			booking.setStatus("CONFIRMED");
			booking = bookingRepository.save(booking);
			log.info("Xác nhận booking thành công, bookingId: {}", bookingId);

			return new BookingDetails(
					booking.getUserEmail(),
					booking.getMovieName(),
					booking.getShowtime(),
					booking.getShowtimeId().toString(),
					Arrays.asList(booking.getSeats().split(",")),
					fetchUser(booking.getUserId().toString()).getUsername(),
					booking.getCreatedAt().toString()
			);
		} catch (Exception e) {
			log.error("Lỗi khi xác nhận booking: {}", e.getMessage());
			throw new RuntimeException("Lỗi khi xác nhận booking: " + e.getMessage());
		}
	}

	public BookingDetails getBookingDetails(String bookingId) {
		try {
			log.info("Lấy chi tiết booking cho bookingId: {}", bookingId);

			Optional<Booking> optionalBooking = bookingRepository.findById(Long.parseLong(bookingId));
			if (!optionalBooking.isPresent()) {
				log.warn("Không tìm thấy booking với bookingId: {}", bookingId);
				return null;
			}

			Booking booking = optionalBooking.get();
			return new BookingDetails(
					booking.getUserEmail(),
					booking.getMovieName(),
					booking.getShowtime(),
					booking.getShowtimeId().toString(),
					Arrays.asList(booking.getSeats().split(",")),
					fetchUser(booking.getUserId().toString()).getUsername(),
					booking.getCreatedAt().toString()
			);
		} catch (Exception e) {
			log.error("Lỗi khi lấy chi tiết booking: {}", e.getMessage());
			return null;
		}
	}

	public void cancelBooking(String bookingId) {
		try {
			log.info("Hủy booking cho bookingId: {}", bookingId);

			Optional<Booking> optionalBooking = bookingRepository.findById(Long.parseLong(bookingId));
			if (!optionalBooking.isPresent()) {
				log.warn("Không tìm thấy booking với bookingId: {}", bookingId);
				return;
			}

			Booking booking = optionalBooking.get();
			booking.setStatus("CANCELLED");
			bookingRepository.save(booking);
			log.info("Hủy booking thành công, bookingId: {}", bookingId);
		} catch (Exception e) {
			log.error("Lỗi khi hủy booking: {}", e.getMessage());
		}
	}

	public UserResponse updateUserInfo(String userId, String name, String email) {
		try {
			log.info("Cập nhật thông tin userId: {}, name: {}, email: {}", userId, name, email);

			UserResponse userResponse = webClientBuilder.build()
					.put()
					.uri("http://user-service:9090/api/user/" + userId)
					.bodyValue(new UpdateUserRequest(name, email))
					.retrieve()
					.bodyToMono(UserResponse.class)
					.block();

			log.info("Cập nhật thông tin user thành công, userId: {}", userId);
			return userResponse;
		} catch (Exception e) {
			log.error("Lỗi khi cập nhật thông tin user: {}", e.getMessage());
			throw new RuntimeException("Lỗi khi cập nhật thông tin người dùng: " + e.getMessage());
		}
	}

	private UserResponse fetchUser(String userId) {
		try {
			return webClientBuilder.build()
					.get()
					.uri("http://user-service:9090/api/user/" + userId)
					.retrieve()
					.bodyToMono(UserResponse.class)
					.block();
		} catch (Exception e) {
			log.error("Lỗi khi lấy thông tin userId: {}: {}", userId, e.getMessage());
			throw new RuntimeException("Lỗi khi lấy thông tin người dùng");
		}
	}

	private String fetchMovieName(String showtimeId) {
		try {
			return webClientBuilder.build()
					.get()
					.uri("http://showtime-service:9090/api/showtimes/" + showtimeId)
					.retrieve()
					.bodyToMono(ShowtimeResponse.class)
					.map(ShowtimeResponse::getMovieName)
					.block();
		} catch (Exception e) {
			log.error("Lỗi khi lấy tên phim cho showtimeId: {}: {}", showtimeId, e.getMessage());
			throw new RuntimeException("Lỗi khi lấy tên phim");
		}
	}

	private String fetchShowtime(String showtimeId) {
		try {
			return webClientBuilder.build()
					.get()
					.uri("http://showtime-service/api/showtimes/" + showtimeId)
					.retrieve()
					.bodyToMono(ShowtimeResponse.class)
					.map(ShowtimeResponse::getShowtime)
					.block();
		} catch (Exception e) {
			log.error("Lỗi khi lấy thời gian suất chiếu cho showtimeId: {}: {}", showtimeId, e.getMessage());
			throw new RuntimeException("Lỗi khi lấy thời gian suất chiếu");
		}
	}
	public List<Booking> getBookingsByShowtimeId(Long showtimeId) {
		return bookingRepository.findByShowtimeId(showtimeId);
	}

}