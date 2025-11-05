package com.workintech.backend.twitter_clone.mapper;

import com.workintech.backend.twitter_clone.dto.CommentResponse;
import com.workintech.backend.twitter_clone.entity.Comment;

public class CommentMapper {
    public static CommentResponse toDto(Comment comment) {
        if (comment == null) return null;
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUserName(),
                comment.getTweet().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
