package com.showtime_service.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidationFilter implements Filter {

	public static final ThreadLocal<Map<String, String>> HEADER_HOLDER = new ThreadLocal<>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Gán headers vào ThreadLocal nếu có (cho FeignClient sử dụng)
		Map<String, String> headersLocal = new HashMap<>();
		String authorizationHeader = httpRequest.getHeader("Authorization");
		String userRoleHeader = httpRequest.getHeader("X-User-Role");
		String userIdHeader = httpRequest.getHeader("X-User-Id");

		if (authorizationHeader != null) {
			headersLocal.put("Authorization", authorizationHeader);
		}
		if (userRoleHeader != null) {
			headersLocal.put("X-User-Role", userRoleHeader);
		}
		if (userIdHeader != null) {
			headersLocal.put("X-User-Id", userIdHeader);
		}

		if (!headersLocal.isEmpty()) {
			HEADER_HOLDER.set(headersLocal);
		}

		// Không kiểm tra token, cho phép qua
		chain.doFilter(request, response);

		// Xoá dữ liệu thread local sau khi xử lý xong
		HEADER_HOLDER.remove();
	}
}
