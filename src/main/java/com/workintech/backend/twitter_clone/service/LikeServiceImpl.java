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
import jakarta.transaction.Transactional;
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
    @Transactional
    public LikeResponse likeTweet(String userName, Long tweetId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        // 🔹 Eğer kullanıcı zaten beğendiyse -> dislike olarak ele al
        if (likeRepository.existsByUserAndTweet(user, tweet)) {
            // mevcut beğeniyi kaldır
            likeRepository.findByUserAndTweet(user, tweet)
                    .ifPresent(likeRepository::delete);
        } else {
            // yeni beğeni kaydı oluştur
            Like like = new Like();
            like.setUser(user);
            like.setTweet(tweet);
            likeRepository.save(like);
        }

        // 🔹 Güncel toplam beğeni sayısını hesapla
        long totalLikes = likeRepository.countByTweet(tweet);

        // 🔹 DTO dön
        Like dummy = new Like();
        dummy.setTweet(tweet);
        dummy.setUser(user);

        return LikeMapper.toDto(dummy, totalLikes);
    }

    @Override
    @Transactional
    public void dislikeTweet(String userName, Long tweetId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        likeRepository.findByUserAndTweet(user, tweet)
                .ifPresent(likeRepository::delete);
    }
}