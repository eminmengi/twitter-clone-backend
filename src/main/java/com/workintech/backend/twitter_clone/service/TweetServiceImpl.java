package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.mapper.TweetMapper;
import com.workintech.backend.twitter_clone.repository.TweetRepository;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TweetService interface'inin implementasyonu.
 * Burada Tweet ile ilgili iş mantığı (business logic) yer alır.
 * Geri dönüş tipi artık DTO (TweetResponse) olmalıdır.
 */
@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public TweetResponse createTweet(String userName, Tweet tweet) {
        // Tweet atmaya çalışan kullanıcıyı bul
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Tweet bilgilerini set et
        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());

        // Veritabanına kaydet
        Tweet savedTweet = tweetRepository.save(tweet);

        // Entity -> DTO dönüşümü
        return TweetMapper.toDto(savedTweet);
    }

    @Override
    public List<TweetResponse> getTweetsByUserName(String userName) {
        // Kullanıcıyı bul
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Kullanıcının tweetlerini getir
        List<Tweet> tweets = tweetRepository.findByUser(user);

        // Hepsini DTO’ya çevirip döndür
        return tweets.stream()
                .map(TweetMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTweet(Long id, String userName) {
        // Silinmek istenen tweet'i bul
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet bulunamadı!"));

        // Sadece tweet sahibi silebilir
        if (!tweet.getUser().getUserName().equals(userName)) {
            throw new RuntimeException("Bu tweeti sadece sahibi silebilir!");
        }

        tweetRepository.delete(tweet);
    }
}