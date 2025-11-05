package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.TweetNotFoundException;
import com.workintech.backend.twitter_clone.exception.UnauthorizedActionException;
import com.workintech.backend.twitter_clone.exception.UserNotFoundException;
import com.workintech.backend.twitter_clone.repository.TweetRepository;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TweetService iş mantığını izole şekilde test ederiz (Spring context açmadan).
 */
class TweetServiceImplTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetServiceImpl tweetService;

    private User user;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Testlerde kullanacağımız user & tweet dummy nesneleri
        user = new User();
        user.setUserName("eminm");

        tweet = new Tweet();
        tweet.setContent("Hello Twitter!");
        tweet.setUser(user);
    }

    @Test
    void createTweet_shouldReturnDto_whenUserExists() {
        // user bulunuyor
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(user));
        // kaydederken repository mock'u tweet döndürsün
        when(tweetRepository.save(any(Tweet.class))).thenAnswer(inv -> inv.getArgument(0));

        // AKSİYON
        TweetResponse res = tweetService.createTweet("eminm", tweet);

        // DOĞRULAMA
        assertNotNull(res);
        assertEquals("Hello Twitter!", res.getContent());
        assertEquals("eminm", res.getUserName());
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    void createTweet_shouldThrow_whenUserNotFound() {
        when(userRepository.findByUserName("unknown")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> tweetService.createTweet("unknown", tweet));
    }

    @Test
    void getTweetsByUserName_shouldReturnListDto() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(user));
        when(tweetRepository.findByUser(user)).thenReturn(List.of(tweet));

        var list = tweetService.getTweetsByUserName("eminm");

        assertEquals(1, list.size());
        assertEquals("eminm", list.get(0).getUserName());
    }

    @Test
    void deleteTweet_shouldDelete_whenOwner() {
        Tweet t = new Tweet();
        t.setId(10L);
        t.setUser(user); // sahibi eminm

        when(tweetRepository.findById(10L)).thenReturn(Optional.of(t));

        // sahibiyiz -> sorun yok
        assertDoesNotThrow(() -> tweetService.deleteTweet(10L, "eminm"));
        verify(tweetRepository, times(1)).delete(t);
    }

    @Test
    void deleteTweet_shouldThrow_whenNotOwner() {
        Tweet t = new Tweet();
        t.setId(10L);
        User other = new User();
        other.setUserName("someoneElse");
        t.setUser(other);

        when(tweetRepository.findById(10L)).thenReturn(Optional.of(t));

        assertThrows(UnauthorizedActionException.class,
                () -> tweetService.deleteTweet(10L, "eminm"));
    }

    @Test
    void deleteTweet_shouldThrow_whenTweetNotFound() {
        when(tweetRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TweetNotFoundException.class,
                () -> tweetService.deleteTweet(99L, "eminm"));
    }
}
