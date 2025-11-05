package com.workintech.backend.twitter_clone.mapper;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.entity.Tweet;

public class TweetMapper {
    public static TweetResponse toDto(Tweet tweet) {
        if (tweet == null) return null;
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getUserName(),
                tweet.getCreatedAt(),
                tweet.getUpdatedAt()
        );
    }
}