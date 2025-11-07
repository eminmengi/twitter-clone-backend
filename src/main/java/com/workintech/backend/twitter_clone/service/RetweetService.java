package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.TweetResponse;

public interface RetweetService {
    TweetResponse retweet(String userName, Long tweetId);
    void undoRetweet(String userName, Long tweetId);
}