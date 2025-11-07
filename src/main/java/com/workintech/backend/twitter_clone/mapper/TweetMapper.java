package com.workintech.backend.twitter_clone.mapper;

import com.workintech.backend.twitter_clone.dto.TweetResponse;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.repository.CommentRepository;
import com.workintech.backend.twitter_clone.repository.LikeRepository;
import com.workintech.backend.twitter_clone.repository.RetweetRepository;

public class TweetMapper {

    public static TweetResponse toDto(
            Tweet tweet,
            User currentUser,
            LikeRepository likeRepository,
            RetweetRepository retweetRepository,
            CommentRepository commentRepository
    ) {
        if (tweet == null) return null;

        String userName = (tweet.getUser() != null)
                ? tweet.getUser().getUserName()
                : "Unknown";

        boolean likedByCurrentUser = false;
        boolean retweetedByCurrentUser = false;

        if (currentUser != null) {
            try {
                likedByCurrentUser = likeRepository.existsByUserAndTweet(currentUser, tweet);
            } catch (Exception ignored) {}
            try {
                retweetedByCurrentUser = retweetRepository.existsByUserAndTweet(currentUser, tweet);
            } catch (Exception ignored) {}
        }

        long totalLikes = likeRepository.countByTweet(tweet);
        long totalRetweets = retweetRepository.countByTweet(tweet);
        long totalComments = commentRepository.countByTweet(tweet);

        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                userName,
                tweet.getCreatedAt(),
                tweet.getUpdatedAt(),
                likedByCurrentUser,
                totalLikes,
                retweetedByCurrentUser,
                totalRetweets,
                totalComments
        );
    }
}