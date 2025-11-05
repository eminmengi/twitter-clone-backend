package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.entity.Tweet;

import java.util.List;

/**
 * Tweet ile ilgili iş mantığı metotlarını tanımlar.
 * Implementation (TweetServiceImpl) bu interface'i uygular.
 */
public interface TweetService {

    Tweet createTweet(String userName, Tweet tweet);

    List<Tweet> getTweetsByUserName(String userName);

    void deleteTweet(Long id, String userName);
}