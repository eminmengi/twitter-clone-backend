package com.workintech.backend.twitter_clone.controller;

import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 🔹 1. Kayıt olma endpointi
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User savedUser = authService.register(user);
        return ResponseEntity.ok(savedUser);
    }

    // 🔹 2. Giriş yapma endpointi
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String userNameOrEmail,
                                      @RequestParam String password) {
        User loggedInUser = authService.login(userNameOrEmail, password);
        return ResponseEntity.ok(loggedInUser);
    }
}
