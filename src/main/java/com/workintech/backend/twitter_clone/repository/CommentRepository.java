package com.workintech.backend.twitter_clone.repository;

import com.workintech.backend.twitter_clone.entity.Comment;
import com.workintech.backend.twitter_clone.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Belirli bir tweet'e ait yorumlar
    List<Comment> findByTweet(Tweet tweet);

    // Tweet'e ait toplam yorum sayısını döner
    long countByTweet(Tweet tweet);
}
