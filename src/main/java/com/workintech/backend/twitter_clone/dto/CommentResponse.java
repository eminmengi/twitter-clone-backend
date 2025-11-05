package com.workintech.backend.twitter_clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Yorum bilgisi + tweet ID + kullanıcı adı bilgisi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String userName;
    private Long tweetId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
