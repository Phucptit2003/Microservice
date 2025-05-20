package com.booking_service.controller;

import com.booking_service.entity.*;
import com.booking_service.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private static final Logger log = LoggerFactory.getLogger(BookingController.class);

	@Autowired
	private BookingService bookingService;

	@Autowired
	private WebClient webClient;

	@Configuration
	public static class WebClientConfig {
		@Bean
		@LoadBalanced
		public WebClient.Builder webClientBuilder() {
			return WebClient.builder();
		}

		@Bean
		public WebClient webClient(WebClient.Builder builder) {
			return builder.build();
		}
	}
	@GetMapping
	public List<Booking> getBookingsByShowtimeId(@RequestParam Long showtimeId) {
		return bookingService.getBookingsByShowtimeId(showtimeId);
	}

	@PostMapping("/create")
	public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
		String bookingId = null;
		try {
			log.info("Tạo booking cho userId: {}, showtimeId: {}, seats: {}, movie: {}, showtimeTime: {}",
					request.getCustomerEmail(), request.getShowtimeId(), request.getSeats(),
					request.getMovie() != null ? request.getMovie().getTitle() : "null",
					request.getShowtimeTime());

			LockSeatRequest lockRequest = new LockSeatRequest(request.getShowtimeId(), request.getSeats());
			log.info("Gửi LockSeatRequest: {}", lockRequest);

			Mono<LockSeatResponse> lockResponse = webClient
					.post()
					.uri("http://seat/api/seats/lock")
					.bodyValue(lockRequest)
					.retrieve()
					.bodyToMono(LockSeatResponse.class)
					.retryWhen(
							Retry.backoff(3, Duration.ofSeconds(1))
									.filter(throwable -> {
										if (throwable instanceof WebClientResponseException ex) {
											return ex.getStatusCode().is5xxServerError();
										}
										return false;
									})
									.doBeforeRetry(retrySignal -> log.warn("Retry lần {} do lỗi: {}",
											retrySignal.totalRetries() + 1, retrySignal.failure().getMessage()))
					)
					.onErrorResume(WebClientResponseException.class, ex -> {
						log.error("Lỗi từ seat service: {}, Response: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
						return Mono.error(new RuntimeException("Lỗi khóa ghế: " + ex.getResponseBodyAsString()));
					});

			LockSeatResponse lockResult = lockResponse.block();
			if (lockResult == null || lockResult.getBookingId() == null) {
				log.warn("Khóa ghế thất bại cho showtimeId: {}, seats: {}",
						request.getShowtimeId(), request.getSeats());
				return ResponseEntity.badRequest().body(new ErrorResponse("Ghế không khả dụng"));
			}

			bookingId = bookingService.createBooking(
					request.getCustomerEmail(),
					request.getShowtimeId(),
					request.getSeats(),
					request.getMovie().getTitle(),
					request.getShowtimeTime()
			);
			log.info("Tạo booking thành công, bookingId: {}", bookingId);

			PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
					.orderId(bookingId)
					.amount((int) (request.getAmount() * 100))
					.currency(request.getCurrency())
					.description("Booking for movie: " + request.getMovie().getTitle())
					.successUrl(request.getSuccessUrl())
					.cancelUrl(request.getCancelUrl())
					.customerEmail(request.getCustomerEmail())
					.build();
			log.info("Gửi paymentRequestDto: {}", paymentRequestDto);

			Mono<PaymentResponseDto> paymentResponseMono = webClient
					.post()
					.uri("http://payment-service/api/payments/create-checkout-session")
					.bodyValue(paymentRequestDto)
					.retrieve()
					.bodyToMono(PaymentResponseDto.class)
					.retryWhen(
							Retry.backoff(3, Duration.ofSeconds(1))
									.filter(throwable -> {
										if (throwable instanceof WebClientResponseException ex) {
											return ex.getStatusCode().is5xxServerError();
										}
										return false;
									})
									.doBeforeRetry(retrySignal -> log.warn("Retry lần {} do lỗi: {}",
											retrySignal.totalRetries() + 1, retrySignal.failure().getMessage()))
					)
					.onErrorResume(WebClientResponseException.class, ex -> {
						log.error("Lỗi từ payment-service: {}, Response: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
						return Mono.error(new RuntimeException("Lỗi tạo checkout session: " + ex.getResponseBodyAsString()));
					});

			PaymentResponseDto paymentResponse = paymentResponseMono.block();
			if (paymentResponse == null || paymentResponse.getCheckoutUrl() == null) {
				log.warn("Tạo liên kết thanh toán thất bại cho bookingId: {}", bookingId);
				bookingService.cancelBooking(bookingId);
				webClient
						.post()
						.uri("http://seat/api/seats/unlock")
						.bodyValue(new LockSeatRequest(request.getShowtimeId(), request.getSeats()))
						.retrieve()
						.bodyToMono(String.class)
						.block();
				return ResponseEntity.badRequest().body(new ErrorResponse("Tạo liên kết thanh toán thất bại"));
			}

			return ResponseEntity.ok(new BookingResponse(bookingId, paymentResponse.getCheckoutUrl()));
		} catch (Exception e) {
			log.error("Lỗi khi tạo booking: {}", e.getMessage(), e);
			if (bookingId != null) {
				bookingService.cancelBooking(bookingId);
				webClient
						.post()
						.uri("http://seat/api/seats/unlock")
						.bodyValue(new LockSeatRequest(request.getShowtimeId(), request.getSeats()))
						.retrieve()
						.bodyToMono(String.class)
						.block();
			}
			return ResponseEntity.badRequest().body(new ErrorResponse("Lỗi khi tạo booking: " + e.getMessage()));
		}
	}

	@PostMapping("/{bookingId}/pay")
	public ResponseEntity<?> processPayment(@PathVariable String bookingId, @RequestBody PaymentRequest paymentRequest) {
		try {
			log.info("Xử lý thanh toán cho bookingId: {}, amount: {}", bookingId, paymentRequest.getAmount());

			// Lấy thông tin booking
			BookingDetails bookingDetails = bookingService.getBookingDetails(bookingId);
			if (bookingDetails == null) {
				log.warn("Không tìm thấy booking với bookingId: {}", bookingId);
				return ResponseEntity.badRequest().body(new ErrorResponse("Không tìm thấy booking"));
			}

			// Tạo PaymentRequestDto
			PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
					.orderId(bookingId)
					.amount((int) (paymentRequest.getAmount() * 100))
					.currency("USD")
					.description("Booking for movie: " + bookingDetails.getMovieName())
					.successUrl("http://localhost:8080/invoice.html?session_id={CHECKOUT_SESSION_ID}") // Chuyển hướng thẳng đến invoice.html
					.cancelUrl("http://localhost:8080/payment/cancel") // Giữ nguyên cancelUrl
					.customerEmail(bookingDetails.getUserEmail())
					.build();
			log.info("Gửi paymentRequestDto: {}", paymentRequestDto);

			// Gửi yêu cầu đến payment-service
			Mono<PaymentResponseDto> paymentResponseMono = webClient
					.post()
					.uri("http://payment-service/api/payments/create-checkout-session")
					.bodyValue(paymentRequestDto)
					.retrieve()
					.bodyToMono(PaymentResponseDto.class)
					.retryWhen(
							Retry.backoff(3, Duration.ofSeconds(1))
									.filter(throwable -> {
										if (throwable instanceof WebClientResponseException ex) {
											return ex.getStatusCode().is5xxServerError();
										}
										return false;
									})
									.doBeforeRetry(retrySignal -> log.warn("Retry lần {} do lỗi: {}",
											retrySignal.totalRetries() + 1, retrySignal.failure().getMessage()))
					)
					.onErrorResume(WebClientResponseException.class, ex -> {
						log.error("Lỗi từ payment-service: {}, Response: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
						return Mono.error(new RuntimeException("Lỗi tạo checkout session: " + ex.getResponseBodyAsString()));
					});

			PaymentResponseDto paymentResponse = paymentResponseMono.block();
			if (paymentResponse == null || paymentResponse.getCheckoutUrl() == null) {
				log.warn("Tạo phiên thanh toán thất bại cho bookingId: {}", bookingId);
				webClient
						.post()
						.uri("http://seat/api/seats/unlock")
						.bodyValue(new LockSeatRequest(bookingDetails.getShowtimeId(), bookingDetails.getSeats()))
						.retrieve()
						.bodyToMono(String.class)
						.block();
				bookingService.cancelBooking(bookingId);
				return ResponseEntity.badRequest().body(new ErrorResponse("Tạo phiên thanh toán thất bại"));
			}

			log.info("Tạo phiên thanh toán thành công, sessionId: {}", paymentResponse.getCheckoutUrl());
			return ResponseEntity.ok(new PaymentResponse(paymentResponse.getCheckoutUrl()));
		} catch (Exception e) {
			log.error("Lỗi khi xử lý thanh toán: {}", e.getMessage(), e);
			try {
				BookingDetails bookingDetails = bookingService.getBookingDetails(bookingId);
				if (bookingDetails != null) {
					webClient
							.post()
							.uri("http://seat/api/seats/unlock")
							.bodyValue(new LockSeatRequest(bookingDetails.getShowtimeId(), bookingDetails.getSeats()))
							.retrieve()
							.bodyToMono(String.class)
							.block();
					log.info("Mở khóa ghế thành công do lỗi thanh toán cho bookingId: {}", bookingId);
				}
				bookingService.cancelBooking(bookingId);
			} catch (Exception unlockEx) {
				log.error("Lỗi khi mở khóa ghế: {}", unlockEx.getMessage(), unlockEx);
			}
			return ResponseEntity.badRequest().body(new ErrorResponse("Lỗi khi xử lý thanh toán: " + e.getMessage()));
		}
	}

	@PostMapping("/{bookingId}/confirm")
	public ResponseEntity<?> confirmBooking(@PathVariable String bookingId, @RequestBody ConfirmRequest confirmRequest) {
		try {
			log.info("Xác nhận booking cho bookingId: {}, sessionId: {}", bookingId, confirmRequest.getSessionId());

			// Call payment-service to get payment status by sessionId
			Mono<java.util.Map> paymentStatus = webClient
					.get()
					.uri("http://payment-service/api/payments/session/" + confirmRequest.getSessionId())
					.retrieve()
					.bodyToMono(java.util.Map.class)
					.retryWhen(
							Retry.backoff(3, Duration.ofSeconds(1))
									.filter(throwable -> throwable instanceof WebClientException)
									.doBeforeRetry(retrySignal -> log.warn("Retry lần {} do lỗi: {}",
											retrySignal.totalRetries() + 1, retrySignal.failure().getMessage()))
					)
					.doOnError(error -> log.error("Lỗi khi gọi payment service: {}", error.getMessage(), error));

			java.util.Map statusResponse = paymentStatus.block();
			String paymentStatusStr = statusResponse != null && statusResponse.get("status") != null ? statusResponse.get("status").toString() : null;
			if (paymentStatusStr == null || !paymentStatusStr.equalsIgnoreCase("COMPLETED")) {
				log.warn("Thanh toán thất bại hoặc bị hủy cho bookingId: {}, status: {}",
						bookingId, paymentStatusStr);

				BookingDetails bookingDetails = bookingService.getBookingDetails(bookingId);
				if (bookingDetails != null) {
					webClient
							.post()
							.uri("http://seat/api/seats/unlock")
							.bodyValue(new LockSeatRequest(bookingDetails.getShowtimeId(), bookingDetails.getSeats()))
							.retrieve()
							.bodyToMono(String.class)
							.block();
					log.info("Mở khóa ghế thành công cho bookingId: {}", bookingId);
				}
				bookingService.cancelBooking(bookingId);

				return ResponseEntity.badRequest().body(new ErrorResponse("Thanh toán thất bại hoặc bị hủy"));
			}

			BookingDetails bookingDetails = bookingService.confirmBooking(bookingId);

			String bookResult = webClient
					.post()
					.uri("http://seat/api/seats/book")
					.bodyValue(new BookSeatRequest(bookingDetails.getShowtimeId(), bookingDetails.getSeats()))
					.retrieve()
					.bodyToMono(String.class)
					.block();

			if (!"Seats booked successfully".equals(bookResult)) {
				log.warn("Đặt ghế thất bại cho bookingId: {}", bookingId);
				bookingService.cancelBooking(bookingId);
				return ResponseEntity.badRequest().body(new ErrorResponse("Đặt ghế thất bại"));
			}

			String emailResult = webClient
					.post()
					.uri("http://notification-service/api/notifications/send")
					.bodyValue(new NotificationRequest(
							bookingDetails.getUserEmail(),
							bookingId,
							bookingDetails.getMovieName(),
							bookingDetails.getShowtime(),
							String.join(",", bookingDetails.getSeats())
					))
					.retrieve()
					.bodyToMono(String.class)
					.block();

			if (!"Email sent successfully".equals(emailResult)) {
				log.warn("Gửi email xác nhận thất bại cho bookingId: {}", bookingId);
			}

			log.info("Xác nhận booking thành công cho bookingId: {}", bookingId);
			return ResponseEntity.ok(bookingDetails);
		} catch (Exception e) {
			log.error("Lỗi khi xác nhận booking: {}", e.getMessage(), e);

			try {
				BookingDetails bookingDetails = bookingService.getBookingDetails(bookingId);
				if (bookingDetails != null) {
					webClient
							.post()
							.uri("http://seat/api/seats/unlock")
							.bodyValue(new LockSeatRequest(bookingDetails.getShowtimeId(), bookingDetails.getSeats()))
							.retrieve()
							.bodyToMono(String.class)
							.block();
					log.info("Mở khóa ghế thành công do lỗi xác nhận bookingId: {}", bookingId);
				}
				bookingService.cancelBooking(bookingId);
			} catch (Exception unlockEx) {
				log.error("Lỗi khi mở khóa ghế: {}", unlockEx.getMessage(), unlockEx);
			}

			return ResponseEntity.badRequest().body(new ErrorResponse("Lỗi khi xác nhận booking: " + e.getMessage()));
		}
	}

	@PutMapping("/user/{userId}")
	public ResponseEntity<?> updateUserInfo(@PathVariable String userId, @RequestBody UpdateUserRequest request) {
		try {
			UserResponse user = bookingService.updateUserInfo(userId, request.getName(), request.getEmail());
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			log.error("Lỗi khi cập nhật thông tin user: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body(new ErrorResponse("Lỗi khi cập nhật thông tin: " + e.getMessage()));
		}
	}
}