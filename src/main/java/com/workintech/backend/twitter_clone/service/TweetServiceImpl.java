package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.TweetNotFoundException;
import com.workintech.backend.twitter_clone.exception.UnauthorizedActionException;
import com.workintech.backend.twitter_clone.exception.UserNotFoundException;
import com.workintech.backend.twitter_clone.mapper.TweetMapper;
import com.workintech.backend.twitter_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TweetService interface'inin implementasyonu.
 * Burada Tweet ile ilgili iş mantığı (business logic) yer alır.
 * Geri dönüş tipi DTO (TweetResponse) kullanır.
 */
@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final RetweetRepository retweetRepository;
    private final CommentRepository commentRepository;

    @Override
    public TweetResponse createTweet(String userName, Tweet tweet) {
        // Tweet atan kullanıcıyı bul
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));

        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());

        Tweet savedTweet = tweetRepository.save(tweet);

        // 🔹 Mapper üzerinden tweet bilgilerini DTO olarak dön
        return TweetMapper.toDto(savedTweet, user, likeRepository, retweetRepository, commentRepository);
    }

    @Override
    public List<TweetResponse> getTweetsByUserName(String userName) {
        // Kullanıcıyı bul
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));

        // Kullanıcının tweetlerini getir
        List<Tweet> tweets = tweetRepository.findByUser(user);

        // 🔹 Mapper ile DTO'ya çevir (beğeni + retweet + yorum bilgisiyle)
        return tweets.stream()
                .map(tweet -> TweetMapper.toDto(tweet, user, likeRepository, retweetRepository, commentRepository))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTweet(Long id, String userName) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        // Silme yetkisi kontrolü
        if (!tweet.getUser().getUserName().equals(userName)) {
            throw new UnauthorizedActionException("Bu tweeti sadece sahibi silebilir!");
        }

        tweetRepository.delete(tweet);
    }

    @Override
    public List<Tweet> getTweetsByUser(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        return tweetRepository.findByUser(user);
    }
    @Override
    public List<TweetResponse> getAllTweets(String currentUserName) {
        User currentUser = userRepository.findByUserName(currentUserName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));

        List<Tweet> allTweets = tweetRepository.findAllByOrderByCreatedAtDesc();

        return allTweets.stream()
                .map(tweet -> TweetMapper.toDto(tweet, currentUser, likeRepository, retweetRepository, commentRepository))
                .collect(Collectors.toList());
    }
}