package com.workintech.backend.twitter_clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    private Long id;
    private String userName;
    private Long tweetId;
    private long totalLikes;
}
