package com.workintech.backend.twitter_clone.controller;

import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.security.JwtService;
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
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User savedUser = authService.register(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String userNameOrEmail,
                                   @RequestParam String password) {

        User loggedInUser = authService.login(userNameOrEmail, password);
        String token = jwtService.generateToken(loggedInUser.getUserName());

        // Token JSON olarak döndürülür
        return ResponseEntity.ok(
                java.util.Map.of(
                        "accessToken", token,
                        "tokenType", "Bearer"
                )
        );
    }
}
