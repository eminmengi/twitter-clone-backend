package com.workintech.backend.twitter_clone.controller;

import com.workintech.backend.twitter_clone.dto.LikeResponse;
import com.workintech.backend.twitter_clone.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 🔹 Tweet beğen
    @PostMapping("/{tweetId}")
    public ResponseEntity<LikeResponse> likeTweet(@PathVariable Long tweetId, Authentication authentication) {
        String userName = authentication.getName();
        LikeResponse response = likeService.likeTweet(userName, tweetId);
        return ResponseEntity.ok(response);
    }

    // 🔹 Tweet beğeniyi geri al (dislike)
    @DeleteMapping("/{tweetId}")
    public ResponseEntity<String> dislikeTweet(@PathVariable Long tweetId, Authentication authentication) {
        String userName = authentication.getName();
        likeService.dislikeTweet(userName, tweetId);
        return ResponseEntity.ok("Beğeni kaldırıldı ✅");
    }
}
