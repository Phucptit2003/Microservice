package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentResponseDto;
import com.example.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentPageController {

    private final PaymentService paymentService;

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("session_id") String sessionId, Model model) {
        log.info("Payment success page requested for session ID: {}", sessionId);
        try {
            PaymentResponseDto payment = paymentService.getPaymentBySessionId(sessionId);
            model.addAttribute("payment", payment);
            return "payment-success";
        } catch (Exception e) {
            log.error("Error retrieving payment for session ID {}: {}", sessionId, e.getMessage());
            return "redirect:/payment/error";
        }
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        log.info("Payment cancel page requested");
        return "payment-cancel";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "payment-error";
    }
} 