package com.workintech.backend.twitter_clone.controller;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.service.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tweet CRUD işlemlerini yönetir.
 * Bu endpoint'ler sadece JWT ile giriş yapmış kullanıcılar içindir.
 */
@RestController
@RequestMapping("/api/tweet")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    // 🔹 Tweet oluşturma
    @PostMapping
    public ResponseEntity<TweetResponse> createTweet(@RequestBody Tweet tweet, Authentication authentication) {
        String userName = authentication.getName(); // JWT'den alınan username
        TweetResponse saved = tweetService.createTweet(userName, tweet);
        return ResponseEntity.ok(saved);
    }

    // 🔹 Kullanıcının tweetlerini listeleme
    @GetMapping("/findByUserName/{userName}")
    public ResponseEntity<List<TweetResponse>> getTweetsByUser(@PathVariable String userName) {
        List<TweetResponse> tweets = tweetService.getTweetsByUserName(userName);
        return ResponseEntity.ok(tweets);
    }

    // 🔹 Tweet silme
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTweet(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        tweetService.deleteTweet(id, userName);
        return ResponseEntity.ok("Tweet silindi ✅");
    }
}