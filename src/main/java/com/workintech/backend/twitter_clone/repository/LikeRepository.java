package com.workintech.backend.twitter_clone.repository;

import com.workintech.backend.twitter_clone.entity.Like;
import com.workintech.backend.twitter_clone.entity.Tweet;
import com.workintech.backend.twitter_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndTweet(User user, Tweet tweet);
    Optional<Like> findByUserAndTweet(User user, Tweet tweet);
    long countByTweet(Tweet tweet); // toplam beğeni sayısı
}
