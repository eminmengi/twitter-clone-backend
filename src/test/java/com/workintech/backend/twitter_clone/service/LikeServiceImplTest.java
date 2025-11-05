package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.LikeResponse;
import com.workintech.backend.twitter_clone.entity.Like;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.ApiException;
import com.workintech.backend.twitter_clone.repository.LikeRepository;
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
 * Like/dislike iş kuralları.
 */
class LikeServiceImplTest {

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LikeServiceImpl likeService;

    private User user;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(); user.setUserName("eminm");
        tweet = new Tweet(); tweet.setId(1L);
    }

    @Test
    void likeTweet_shouldReturnDto_whenOk() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(user));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(likeRepository.existsByUserAndTweet(user, tweet)).thenReturn(false);
        when(likeRepository.save(any(Like.class))).thenAnswer(inv -> inv.getArgument(0));
        when(likeRepository.countByTweet(tweet)).thenReturn(3L);

        LikeResponse res = likeService.likeTweet("eminm", 1L);

        assertEquals("eminm", res.getUserName());
        assertEquals(1L, res.getTweetId());
        assertEquals(3L, res.getTotalLikes());
    }

    @Test
    void likeTweet_shouldThrow_whenAlreadyLiked() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(user));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(likeRepository.existsByUserAndTweet(user, tweet)).thenReturn(true);

        assertThrows(ApiException.class,
                () -> likeService.likeTweet("eminm", 1L));
    }

    @Test
    void dislikeTweet_shouldThrow_whenNotFound() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(user));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(likeRepository.findByUserAndTweet(user, tweet)).thenReturn(Optional.empty());

        assertThrows(ApiException.class,
                () -> likeService.dislikeTweet("eminm", 1L));
    }
}
