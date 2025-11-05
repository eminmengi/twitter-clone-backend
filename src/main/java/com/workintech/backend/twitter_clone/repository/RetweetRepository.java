package com.workintech.backend.twitter_clone.repository;

import com.workintech.backend.twitter_clone.entity.Retweet;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    boolean existsByUserAndTweet(User user, Tweet tweet);
    Optional<Retweet> findByUserAndTweet(User user, Tweet tweet);
    long countByTweet(Tweet tweet);
}