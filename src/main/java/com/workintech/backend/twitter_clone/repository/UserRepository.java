package com.workintech.backend.twitter_clone.repository;

import com.workintech.backend.twitter_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Bu interface veritabanında "users" tablosu ile iletişimi sağlar.
 * JpaRepository, CRUD işlemlerini (save, findAll, findById, delete) otomatik getirir.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);

}
