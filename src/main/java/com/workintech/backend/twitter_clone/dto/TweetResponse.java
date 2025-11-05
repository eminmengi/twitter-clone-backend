package com.workintech.backend.twitter_clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Tweet ve sahibi hakkında minimal bilgi döner.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponse {
    private Long id;
    private String content;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
