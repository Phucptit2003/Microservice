package com.api_gateway.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class JwtAuthFilter {

    // Không cần danh sách route công khai nữa vì không cần xác thực
    @Bean
    public GlobalFilter globalJwtFilter() {
        return (exchange, chain) -> {
            // Bỏ qua xác thực và cho phép tiếp tục các request
            return chain.filter(exchange);
        };
    }
}
