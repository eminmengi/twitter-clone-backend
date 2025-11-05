package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.LikeResponse;
import com.workintech.backend.twitter_clone.entity.Like;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.ApiException;
import com.workintech.backend.twitter_clone.exception.TweetNotFoundException;
import com.workintech.backend.twitter_clone.exception.UserNotFoundException;
import com.workintech.backend.twitter_clone.mapper.LikeMapper;
import com.workintech.backend.twitter_clone.repository.LikeRepository;
import com.workintech.backend.twitter_clone.repository.TweetRepository;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public LikeResponse likeTweet(String userName, Long tweetId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        if (likeRepository.existsByUserAndTweet(user, tweet)) {
            throw new ApiException("Bu tweet zaten beğenilmiş!", HttpStatus.BAD_REQUEST);
        }

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        Like saved = likeRepository.save(like);

        long totalLikes = likeRepository.countByTweet(tweet);
        return LikeMapper.toDto(saved, totalLikes);
    }

    @Override
    public void dislikeTweet(String userName, Long tweetId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        Like like = likeRepository.findByUserAndTweet(user, tweet)
                .orElseThrow(() -> new ApiException("Beğeni bulunamadı!", HttpStatus.NOT_FOUND));

        likeRepository.delete(like);
    }
}
