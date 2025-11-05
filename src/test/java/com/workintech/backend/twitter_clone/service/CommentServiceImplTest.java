package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.CommentResponse;
import com.workintech.backend.twitter_clone.entity.Comment;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.TweetNotFoundException;
import com.workintech.backend.twitter_clone.exception.UnauthorizedActionException;
import com.workintech.backend.twitter_clone.exception.UserNotFoundException;
import com.workintech.backend.twitter_clone.repository.CommentRepository;
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
 * Yorum ekleme/silme/güncelleme iş kuralları testleri.
 */
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User owner;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        owner = new User();
        owner.setUserName("eminm");

        tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser(owner);
    }

    @Test
    void addComment_shouldReturnDto_whenOk() {
        Comment c = new Comment();
        c.setContent("nice!");

        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(owner));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        CommentResponse res = commentService.addComment("eminm", 1L, c);

        assertEquals("nice!", res.getContent());
        assertEquals(1L, res.getTweetId());
        assertEquals("eminm", res.getUserName());
    }

    @Test
    void addComment_shouldThrow_whenUserNotFound() {
        when(userRepository.findByUserName("x")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> commentService.addComment("x", 1L, new Comment()));
    }

    @Test
    void addComment_shouldThrow_whenTweetNotFound() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(owner));
        when(tweetRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TweetNotFoundException.class,
                () -> commentService.addComment("eminm", 99L, new Comment()));
    }

    @Test
    void deleteComment_shouldThrow_whenUnauthorizedUser() {
        // comment sahibi = "ali", tweet sahibi = "eminm", silmeye çalışan = "veli" -> yetkisiz
        User commentOwner = new User(); commentOwner.setUserName("ali");

        Comment c = new Comment();
        c.setId(10L);
        c.setUser(commentOwner);
        c.setTweet(tweet);

        when(commentRepository.findById(10L)).thenReturn(Optional.of(c));

        assertThrows(UnauthorizedActionException.class,
                () -> commentService.deleteComment(10L, "veli"));
    }
}
