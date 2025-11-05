package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.CommentResponse;
import com.workintech.backend.twitter_clone.entity.Comment;

import java.util.List;

public interface CommentService {

    CommentResponse addComment(String userName, Long tweetId, Comment comment);
    CommentResponse updateComment(Long id, String userName, Comment updatedComment);
    void deleteComment(Long id, String userName);
    List<CommentResponse> getCommentsByTweet(Long tweetId);
}
