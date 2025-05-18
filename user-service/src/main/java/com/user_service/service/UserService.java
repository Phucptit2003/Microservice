package com.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user_service.entity.Client;
import com.user_service.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	@Lazy
	private PasswordEncoder passEncoder;
	
	public Client registerUser(String username, String password, String role, String email) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Client user = new Client();
        user.setUsername(username);
        user.setPassword(passEncoder.encode(password));
        user.setRole(role);
        user.setEmail(email);

        return userRepo.save(user);
    }

    public Client findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }
    public Client findById(String id) {
        return userRepo.findById(Long.parseLong(id.trim())).orElse(null);
    }
    public Client findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Email not found"));
    }
}
