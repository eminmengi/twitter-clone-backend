package com.workintech.backend.twitter_clone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/secure")
    public String secureEndpoint() {
        return "Bu sayfa sadece giriş yapmış kullanıcıya görünür";
    }
}