package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.entity.Retweet;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.ApiException;
import com.workintech.backend.twitter_clone.exception.TweetNotFoundException;
import com.workintech.backend.twitter_clone.exception.UserNotFoundException;
import com.workintech.backend.twitter_clone.mapper.TweetMapper;
import com.workintech.backend.twitter_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Override
    public TweetResponse retweet(String userName, Long tweetId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        Tweet original = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        if (retweetRepository.existsByUserAndTweet(user, original)) {
            throw new ApiException("Bu tweet zaten retweet edilmiş!", HttpStatus.BAD_REQUEST);
        }

        //RT tweet olarak yeni Tweet kaydı oluştur
        Tweet retweetTweet = new Tweet();
        retweetTweet.setUser(user);
        retweetTweet.setCreatedAt(LocalDateTime.now());
        retweetTweet.setContent("@" + original.getUser().getUserName() + ": " + original.getContent());

        Tweet savedRetweetTweet = tweetRepository.save(retweetTweet);

        //Retweet tablosuna da kaydet (ilişki takibi için)
        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(original);
        retweetRepository.save(retweet);

        //Geriye yeni tweet gibi dön
        return TweetMapper.toDto(savedRetweetTweet, user, likeRepository, retweetRepository, commentRepository);
    }

    @Override
    public void undoRetweet(String userName, Long tweetId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        Tweet original = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        // RT kaydını bul
        Retweet retweet = retweetRepository.findByUserAndTweet(user, original)
                .orElseThrow(() -> new ApiException("Retweet bulunamadı!", HttpStatus.NOT_FOUND));

        // RT tweet'i de sil
        Tweet rtTweet = tweetRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(t -> t.getUser().equals(user) && t.getContent().startsWith("@" + original.getUser().getUserName()))
                .findFirst()
                .orElse(null);
        if (rtTweet != null) {
            tweetRepository.delete(rtTweet);
        }

        retweetRepository.delete(retweet);
    }
}
