package com.workintech.backend.twitter_clone.controller;

import com.workintech.backend.twitter_clone.dto.UserResponse;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.security.JwtService;
import com.workintech.backend.twitter_clone.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 🔹 Register
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody User user) {
        UserResponse response = authService.register(user);
        return ResponseEntity.ok(response);
    }

    // 🔹 Login (token döner)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String userNameOrEmail,
                                   @RequestParam String password) {
        String token = authService.login(userNameOrEmail, password);
        return ResponseEntity.ok(
                new Object() {
                    public final String accessToken = token;
                    public final String tokenType = "Bearer";
                }
        );
    }
}