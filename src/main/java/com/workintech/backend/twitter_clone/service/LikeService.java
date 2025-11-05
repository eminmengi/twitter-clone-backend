package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.LikeResponse;

public interface LikeService {

    LikeResponse likeTweet(String userName, Long tweetId);

    void dislikeTweet(String userName, Long tweetId);
}
