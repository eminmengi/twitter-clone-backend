package com.workintech.backend.twitter_clone.mapper;

import com.workintech.backend.twitter_clone.dto.RetweetResponse;
import com.workintech.backend.twitter_clone.entity.Retweet;

public class RetweetMapper {
    public static RetweetResponse toDto(Retweet retweet, long totalRetweets) {
        if (retweet == null) return null;

        String userName = null;
        Long tweetId = null;

        //LazyInitializationException önleme
        try {
            if (retweet.getUser() != null) {
                userName = retweet.getUser().getUserName();
            }
        } catch (Exception e) {
            userName = "Unknown";
        }

        try {
            if (retweet.getTweet() != null) {
                tweetId = retweet.getTweet().getId();
            }
        } catch (Exception e) {
            tweetId = null;
        }

        // 🔹 DTO dönüşümü
        return new RetweetResponse(
                retweet.getId(),
                userName,
                tweetId,
                totalRetweets,
                retweet.getCreatedAt()
        );
    }
}
