package com.example.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private Long showtimeId;
	private String seats; // Lưu dạng "A1,A2"
	private String status; // PENDING, CONFIRMED, CANCELLED
	private String movieName;
	private String showtime;
	private String userEmail;
	private LocalDateTime createdAt;
	private double totalPrice;

}