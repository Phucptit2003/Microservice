package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentRequestDto;
import com.example.paymentservice.dto.PaymentResponseDto;
import com.example.paymentservice.exception.PaymentException;
import com.example.paymentservice.model.Payment.PaymentStatus;
import com.example.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<PaymentResponseDto> createCheckoutSession(
            @Valid @RequestBody PaymentRequestDto paymentRequestDto, BindingResult bindingResult) {
        log.info("Entered createCheckoutSession with orderId: {}", paymentRequestDto.getOrderId());

        if (bindingResult.hasErrors()) {
            log.error("Validation errors for PaymentRequestDto: {}", bindingResult.getAllErrors());
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            log.warn("Returning validation failure for orderId: {}: {}", paymentRequestDto.getOrderId(), errorMessage);
            return ResponseEntity.badRequest().body(
                    PaymentResponseDto.builder()
                            .message("Validation failed: " + errorMessage)
                            .build()
            );
        }
        try {
            log.info("Creating checkout session for order: {}", paymentRequestDto.getOrderId());
            PaymentResponseDto response = paymentService.createCheckoutSession(paymentRequestDto);
            log.info("Successfully created checkout session for order: {}", paymentRequestDto.getOrderId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Exception while creating checkout session for order: {}", paymentRequestDto.getOrderId(), e);
            throw e;
        }
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<PaymentResponseDto> getPaymentBySessionId(@PathVariable String sessionId) {
        log.info("Retrieving payment for session ID: {}", sessionId);
        PaymentResponseDto response = paymentService.getPaymentBySessionId(sessionId);
        // Check if the session is expired (older than 5 minutes)
        if (response.getCreatedAt() != null &&
            response.getCreatedAt().plusMinutes(5).isBefore(java.time.LocalDateTime.now()) &&
            !"COMPLETED".equalsIgnoreCase(String.valueOf(response.getStatus()))) {
            response.setStatus(com.example.paymentservice.model.Payment.PaymentStatus.EXPIRED);
            response.setMessage("Payment link expired");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        log.info("Received webhook from Stripe");
        String response = paymentService.handleWebhookEvent(payload, sigHeader);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/retry")
    public String retryPayment() {
        // In a real application, you might want to retrieve the last failed payment
        // and redirect to a form to retry the payment
        return "redirect:/";
    }
    
    @PostMapping("/update-status/{paymentId}")
    public ResponseEntity<Map<String, Object>> updatePaymentStatus(
            @PathVariable String paymentId,
            @RequestParam PaymentStatus status) {
        log.info("Manually updating payment status for ID: {} to {}", paymentId, status);
        try {
            PaymentResponseDto response = paymentService.updatePaymentStatus(paymentId, status);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Payment status updated successfully");
            result.put("payment", response);
            return ResponseEntity.ok(result);
        } catch (PaymentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(error);
        }
    }
} 