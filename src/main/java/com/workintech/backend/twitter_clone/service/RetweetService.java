package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.RetweetResponse;

public interface RetweetService {
    RetweetResponse retweet(String userName, Long tweetId);
    void undoRetweet(String userName, Long tweetId);
}