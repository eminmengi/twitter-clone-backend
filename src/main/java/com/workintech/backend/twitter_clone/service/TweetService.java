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

    List<TweetResponse> getAllTweets(String currentUserName);


    //    Controller’ın mapper ile kullanabilmesi için entity dönen ek metot
    //    Not: Bu metot, controller’ın currentUser & likeRepository ile
    //    likedByCurrentUser/totalLikes hesaplamasını yapabilmesi için eklendi.
    List<Tweet> getTweetsByUser(String userName);
}