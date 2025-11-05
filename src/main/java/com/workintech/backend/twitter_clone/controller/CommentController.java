package com.workintech.backend.twitter_clone.controller;

import com.workintech.backend.twitter_clone.dto.CommentResponse;
import com.workintech.backend.twitter_clone.entity.Comment;
import com.workintech.backend.twitter_clone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 🔹 Yorum ekleme
    @PostMapping("/{tweetId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long tweetId,
                                                      @RequestBody Comment comment,
                                                      Authentication authentication) {
        String userName = authentication.getName();
        CommentResponse response = commentService.addComment(userName, tweetId, comment);
        return ResponseEntity.ok(response);
    }

    // 🔹 Yorum güncelleme
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id,
                                                         @RequestBody Comment comment,
                                                         Authentication authentication) {
        String userName = authentication.getName();
        CommentResponse response = commentService.updateComment(id, userName, comment);
        return ResponseEntity.ok(response);
    }

    // 🔹 Yorum silme
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id,
                                                Authentication authentication) {
        String userName = authentication.getName();
        commentService.deleteComment(id, userName);
        return ResponseEntity.ok("Yorum silindi ✅");
    }

    // 🔹 Tweet'e ait yorumları listeleme
    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByTweet(@PathVariable Long tweetId) {
        List<CommentResponse> comments = commentService.getCommentsByTweet(tweetId);
        return ResponseEntity.ok(comments);
    }
}