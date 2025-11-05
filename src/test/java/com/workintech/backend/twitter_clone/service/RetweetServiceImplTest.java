package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.RetweetResponse;
import com.workintech.backend.twitter_clone.entity.Retweet;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.ApiException;
import com.workintech.backend.twitter_clone.repository.RetweetRepository;
import com.workintech.backend.twitter_clone.repository.TweetRepository;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Retweet ve geri alma kuralları testleri.
 */
class RetweetServiceImplTest {

    @Mock
    private RetweetRepository retweetRepository;
    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RetweetServiceImpl retweetService;

    private User user;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(); user.setUserName("eminm");
        tweet = new Tweet(); tweet.setId(1L);
    }

    @Test
    void retweet_shouldReturnDto_whenOk() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(user));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.existsByUserAndTweet(user, tweet)).thenReturn(false);
        when(retweetRepository.save(any(Retweet.class))).thenAnswer(inv -> inv.getArgument(0));
        when(retweetRepository.countByTweet(tweet)).thenReturn(2L);

        RetweetResponse res = retweetService.retweet("eminm", 1L);

        assertEquals("eminm", res.getUserName());
        assertEquals(1L, res.getTweetId());
        assertEquals(2L, res.getTotalRetweets());
    }

    @Test
    void retweet_shouldThrow_whenAlreadyRetweeted() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(user));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(retweetRepository.existsByUserAndTweet(user, tweet)).thenReturn(true);

        assertThrows(ApiException.class,
                () -> retweetService.retweet("eminm", 1L));
    }
}
