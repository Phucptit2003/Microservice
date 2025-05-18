package com.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotificationDto {
    private String customerEmail;
    private String orderId;
    private String paymentId;
    private BigDecimal amount;
    private String currency;
    private String description;
    private LocalDateTime paymentDate;
    private String paymentMethod;
} 