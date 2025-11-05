package com.workintech.backend.twitter_clone.repository;

import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    // Belirli bir kullanıcıya ait tüm tweetler
    List<Tweet> findByUser(User user);
}