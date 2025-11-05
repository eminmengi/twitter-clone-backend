package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.UserResponse;
import com.workintech.backend.twitter_clone.entity.User;

/**
 * Authentication (register & login) işlemleri için interface.
 * Gerçek işlemler AuthServiceImpl'de uygulanır.
 */
public interface AuthService {
    UserResponse register(User user);
    String login(String userNameOrEmail, String password);
}
