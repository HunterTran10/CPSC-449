package com.example.bookstore_mongodb.controller;

import com.example.bookstore_mongodb.entity.User;
import com.example.bookstore_mongodb.repository.UserRepository;
import com.example.bookstore_mongodb.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authManager;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtUtil jwtUtil;

    // ── POST /api/auth/register ───────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // Check if username already taken
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username already exists"));
        }

        // Hash the password with BCrypt before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role if none provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("USER"));
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    // ── POST /api/auth/login ──────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        // Validate credentials — throws exception if wrong
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Load full UserDetails (with roles) and generate token
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(loginRequest.getUsername());

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token));
    }
}
