package com.workintech.backend.twitter_clone.mapper;

import com.workintech.backend.twitter_clone.dto.LikeResponse;
import com.workintech.backend.twitter_clone.entity.Like;

public class LikeMapper {
    public static LikeResponse toDto(Like like, long totalLikes) {

        if (like == null) return null;

        return new LikeResponse(
                like.getId(),
                like.getUser().getUserName(),
                like.getTweet().getId(),
                totalLikes
        );
    }
}
