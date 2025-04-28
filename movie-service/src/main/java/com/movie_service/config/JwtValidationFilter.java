package com.movie_service.config;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidationFilter implements Filter {

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request,
                         jakarta.servlet.ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Nếu cần debug các header (tùy chọn)
        String authorizationHeader = httpRequest.getHeader("Authorization");
        String userRoleHeader = httpRequest.getHeader("X-User-Role");
        String userIdHeader = httpRequest.getHeader("X-User-Id");

        // In ra console cho kiểm tra nếu cần (có thể xoá nếu không cần log)
        System.out.println("Authorization: " + authorizationHeader);
        System.out.println("X-User-Role: " + userRoleHeader);
        System.out.println("X-User-Id: " + userIdHeader);

        // Bỏ xác thực, cho phép tất cả request qua
        chain.doFilter(request, response);
    }
}
