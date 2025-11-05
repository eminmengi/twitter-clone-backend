package com.workintech.backend.twitter_clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Kullanıcı bilgilerini frontend'e döndürmek için DTO.
 * Şifre gibi hassas alanlar burada bulunmaz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String userName;
    private String email;
    private String bio;
    private String avatar;
    private String role;
    private LocalDateTime createdAt;
}
