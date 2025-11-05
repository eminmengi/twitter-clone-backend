package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.RetweetResponse;
import com.workintech.backend.twitter_clone.entity.Retweet;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.mapper.RetweetMapper;
import com.workintech.backend.twitter_clone.repository.RetweetRepository;
import com.workintech.backend.twitter_clone.repository.TweetRepository;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public RetweetResponse retweet(String userName, Long tweetId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet bulunamadı!"));

        if (retweetRepository.existsByUserAndTweet(user, tweet)) {
            throw new RuntimeException("Bu tweet zaten retweet edilmiş!");
        }

        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);

        Retweet saved = retweetRepository.save(retweet);
        long totalRetweets = retweetRepository.countByTweet(tweet);

        return RetweetMapper.toDto(saved, totalRetweets);
    }

    @Override
    public void undoRetweet(String userName, Long tweetId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet bulunamadı!"));

        Retweet retweet = retweetRepository.findByUserAndTweet(user, tweet)
                .orElseThrow(() -> new RuntimeException("Retweet bulunamadı!"));

        retweetRepository.delete(retweet);
    }
}
