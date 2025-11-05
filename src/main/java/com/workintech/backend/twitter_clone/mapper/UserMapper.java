package com.workintech.backend.twitter_clone.mapper;

import com.workintech.backend.twitter_clone.dto.UserResponse;
import com.workintech.backend.twitter_clone.entity.User;

/**
 * Entity → DTO dönüştürür
 */

public class UserMapper {
    public static UserResponse toDto(User user) {
        if (user == null) return null;
        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getBio(),
                user.getAvatar(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}
