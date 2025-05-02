package com.example.payment.controller;

import com.example.payment.model.PaymentRequest;
import com.example.payment.model.PaymentResponse;
import com.example.payment.model.PaymentStatusResponse;
import com.example.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) throws StripeException {
        String sessionId = paymentService.createPaymentSession(request.getBookingId(), request.getAmount());
        return ResponseEntity.ok(new PaymentResponse(sessionId));
    }

    @GetMapping("/status/{sessionId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String sessionId) throws StripeException {
        String status = paymentService.getPaymentStatus(sessionId);
        return ResponseEntity.ok(new PaymentStatusResponse(status));
    }

}

