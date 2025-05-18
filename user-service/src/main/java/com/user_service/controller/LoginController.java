package com.user_service.controller;

import com.user_service.entity.Client;
import com.user_service.entity.UserSignInDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/login")
    public String showLoginForm() {
        return "index"; // Trả về template login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            // Gửi request tới API đăng nhập
            String url = "http://user-service:9090/api/user/login";
            UserSignInDTO loginRequest = new UserSignInDTO();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            ResponseEntity<String> response = restTemplate.postForEntity(url, loginRequest, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String token = response.getBody();
                model.addAttribute("token", token);
                return "index";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "index";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "index";
        }
    }
}