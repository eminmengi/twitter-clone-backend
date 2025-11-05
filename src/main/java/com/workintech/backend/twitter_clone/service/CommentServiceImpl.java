package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.CommentResponse;
import com.workintech.backend.twitter_clone.entity.Comment;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.TweetNotFoundException;
import com.workintech.backend.twitter_clone.exception.UnauthorizedActionException;
import com.workintech.backend.twitter_clone.exception.UserNotFoundException;
import com.workintech.backend.twitter_clone.mapper.CommentMapper;
import com.workintech.backend.twitter_clone.repository.CommentRepository;
import com.workintech.backend.twitter_clone.repository.TweetRepository;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse addComment(String userName, Long tweetId, Comment comment) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        comment.setUser(user);
        comment.setTweet(tweet);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return CommentMapper.toDto(saved);
    }

    @Override
    public CommentResponse updateComment(Long id, String userName, Comment updated) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new TweetNotFoundException("Yorum bulunamadı!"));

        if (!comment.getUser().getUserName().equals(userName)) {
            throw new UnauthorizedActionException("Bu yorumu sadece sahibi düzenleyebilir!");
        }

        comment.setContent(updated.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long id, String userName) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new TweetNotFoundException("Yorum bulunamadı!"));

        String commentOwner = comment.getUser().getUserName();
        String tweetOwner = comment.getTweet().getUser().getUserName();

        if (!userName.equals(commentOwner) && !userName.equals(tweetOwner)) {
            throw new UnauthorizedActionException("Bu yorumu yalnızca tweet veya yorum sahibi silebilir!");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByTweet(Long tweetId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı!"));

        return commentRepository.findByTweet(tweet)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}