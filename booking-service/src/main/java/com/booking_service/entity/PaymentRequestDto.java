package com.booking_service.entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private double amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    private String description;

    @NotBlank(message = "Success URL is required")
    private String successUrl;

    @NotBlank(message = "Cancel URL is required")
    private String cancelUrl;

    private String customerEmail;
}