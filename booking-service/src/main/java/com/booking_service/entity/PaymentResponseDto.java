package com.booking_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private String paymentId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private Payment.PaymentStatus status;
    private String checkoutUrl;
    private String sessionId;
    private String message;
    private LocalDateTime createdAt;
}