package com.user_service.controller;

import com.user_service.entity.Client;
import com.user_service.entity.LoginResponse;
import com.user_service.entity.UserSignInDTO;
import com.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/test")
	public String test() {
		return "Private Access";
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Client user) {
		return ResponseEntity.ok(userService.registerUser(user.getUsername(), user.getPassword(), user.getRole(),user.getEmail()));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserSignInDTO user) {
		log.info("Nhận yêu cầu đăng nhập cho username: {}", user.getUsername());
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
			);

			Client client = userService.findByUsername(user.getUsername());
			if (client == null || client.getId() == null) {
				log.error("Không tìm thấy người dùng hoặc userId cho username: {}", user.getUsername());
				return ResponseEntity.status(500).body("Không tìm thấy userId cho người dùng");
			}

			Client userReponse = userService.findByUsername(user.getUsername());
			String token = "12345"; // TODO: Thay bằng JwtUtil khi tích hợp
			log.info("Đăng nhập thành công, userId: {}, token: {}", userReponse.getId(), token);
			return ResponseEntity.ok(new LoginResponse(token, userReponse));
		} catch (AuthenticationException e) {
			log.error("Đăng nhập thất bại cho username: {}. Lỗi: {}", user.getUsername(), e.getMessage());
			return ResponseEntity.status(401).body("Thông tin đăng nhập không hợp lệ");
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUser(@PathVariable String userId, HttpServletRequest request) {
		log.info("Yêu cầu lấy thông tin userId: {}, Origin: {}", userId, request.getHeader("Origin"));
		try {
			Client user = userService.findById(userId);
			if (user == null) {
				log.warn("Không tìm thấy người dùng với userId: {}", userId);
				return ResponseEntity.status(404).body("Không tìm thấy người dùng");
			}
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			log.error("Lỗi khi lấy thông tin người dùng với userId: {}. Lỗi: {}", userId, e.getMessage());
			return ResponseEntity.status(500).body("Lỗi khi lấy thông tin người dùng: " + e.getMessage());
		}
	}
}