package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.repository.TweetRepository;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TweetService interface'inin gerçek implementasyonudur.
 * Burada tüm iş mantığı (business logic) yer alır.
 */
@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public Tweet createTweet(String userName, Tweet tweet) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());
        return tweetRepository.save(tweet);
    }

    @Override
    public List<Tweet> getTweetsByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        return tweetRepository.findByUser(user);
    }

    @Override
    public void deleteTweet(Long id, String userName) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet bulunamadı!"));

        if (!tweet.getUser().getUserName().equals(userName)) {
            throw new RuntimeException("Bu tweeti sadece sahibi silebilir!");
        }
        tweetRepository.delete(tweet);
    }
}
