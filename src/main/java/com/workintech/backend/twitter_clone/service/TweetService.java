package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.entity.Tweet;

import java.util.List;

/**
 * Tweet işlemlerinin arayüzü.
 * Artık Tweet yerine TweetResponse döndürür.
 */
public interface TweetService {
    TweetResponse createTweet(String userName, Tweet tweet);
    List<TweetResponse> getTweetsByUserName(String userName);
    void deleteTweet(Long id, String userName);
}