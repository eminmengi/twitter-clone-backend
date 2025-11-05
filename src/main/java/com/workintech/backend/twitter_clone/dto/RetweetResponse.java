package com.workintech.backend.twitter_clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetweetResponse {
    private Long id;
    private String userName;
    private Long tweetId;
    private long totalRetweets;
    private LocalDateTime createdAt;
}
