package com.example.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for handling payment requests from clients
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotBlank(message = "Currency is required")
    private String currency;
    
    private String description;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Success URL is required")
    private String successUrl;
    
    @NotBlank(message = "Cancel URL is required")
    private String cancelUrl;
    
    private String customerEmail;
} 