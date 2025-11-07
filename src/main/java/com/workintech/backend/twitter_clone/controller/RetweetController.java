package com.workintech.backend.twitter_clone.controller;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.service.RetweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/retweet")
@RequiredArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;

    // Retweet at
    @PostMapping("/{tweetId}")
    public ResponseEntity<TweetResponse> retweet(
            @PathVariable Long tweetId,
            Authentication authentication
    ) {
        String userName = authentication.getName();
        // TweetResponse dönecek
        return ResponseEntity.ok(retweetService.retweet(userName, tweetId));
    }

    // Retweet'i geri al
    @DeleteMapping("/{tweetId}")
    public ResponseEntity<String> undoRetweet(
            @PathVariable Long tweetId,
            Authentication authentication
    ) {
        String userName = authentication.getName();
        retweetService.undoRetweet(userName, tweetId);
        return ResponseEntity.ok("Retweet kaldırıldı");
    }
}
