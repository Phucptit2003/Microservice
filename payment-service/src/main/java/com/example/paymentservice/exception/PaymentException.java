package com.example.paymentservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentException extends RuntimeException {

    private final HttpStatus status;

    public PaymentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public PaymentException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}