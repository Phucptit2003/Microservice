package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequestDto;
import com.example.paymentservice.dto.PaymentResponseDto;
import com.example.paymentservice.model.Payment.PaymentStatus;

public interface PaymentService {
    
    /**
     * Creates a payment session for checkout and returns the checkout URL
     * 
     * @param paymentRequestDto the payment request details
     * @return PaymentResponseDto containing the checkout URL and session info
     */
    PaymentResponseDto createCheckoutSession(PaymentRequestDto paymentRequestDto);
    
    /**
     * Handles the webhook event from Stripe after payment completion
     * 
     * @param payload the webhook payload
     * @param sigHeader the Stripe signature header
     * @return String response message
     */
    String handleWebhookEvent(String payload, String sigHeader);
    
    /**
     * Retrieves payment details by session ID
     * 
     * @param sessionId the Stripe session ID
     * @return PaymentResponseDto containing payment details
     */
    PaymentResponseDto getPaymentBySessionId(String sessionId);
    
    /**
     * Manually updates payment status
     * 
     * @param paymentId the payment ID
     * @param status the new payment status
     * @return PaymentResponseDto containing updated payment details
     */
    PaymentResponseDto updatePaymentStatus(String paymentId, PaymentStatus status);
}
