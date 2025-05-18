package com.example.paymentservice.client;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    
    @GetMapping("/api/user/")
    Object getUserByEmail(@RequestParam("email") String email) throws FeignException;
} 