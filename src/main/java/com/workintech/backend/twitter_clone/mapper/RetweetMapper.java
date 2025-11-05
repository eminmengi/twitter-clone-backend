package com.workintech.backend.twitter_clone.mapper;

import com.workintech.backend.twitter_clone.dto.RetweetResponse;
import com.workintech.backend.twitter_clone.entity.Retweet;

public class RetweetMapper {
    public static RetweetResponse toDto(Retweet retweet, long totalRetweets) {
        if (retweet == null) return null;
        return new RetweetResponse(
                retweet.getId(),
                retweet.getUser().getUserName(),
                retweet.getTweet().getId(),
                totalRetweets,
                retweet.getCreatedAt()
        );
    }
}
