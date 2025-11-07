package com.workintech.backend.twitter_clone.mapper;

import com.workintech.backend.twitter_clone.dto.CommentResponse;
import com.workintech.backend.twitter_clone.entity.Comment;

public class CommentMapper {
    public static CommentResponse toDto(Comment comment) {
        if (comment == null) return null;

        String userName = null;
        Long tweetId = null;

        //Hibernate Lazy proxy hatasına karşı
        try {
            if (comment.getUser() != null) {
                userName = comment.getUser().getUserName();
            }
        } catch (Exception e) {
            userName = "Unknown";
        }

        try {
            if (comment.getTweet() != null) {
                tweetId = comment.getTweet().getId();
            }
        } catch (Exception e) {
            tweetId = null;
        }

        // 🔹 DTO dönüşümü
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                userName,
                tweetId,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
