package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.dto.UserResponse;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.mapper.UserMapper;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import com.workintech.backend.twitter_clone.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Kullanıcı kayıt & giriş işlemleri.
 * DTO ve JWT mantığı ile çalışır.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponse register(User user) {
        userRepository.findByUserName(user.getUserName()).ifPresent(u -> {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor!");
        });
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Bu e-posta zaten kayıtlı!");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        return UserMapper.toDto(saved);
    }

    @Override
    public String login(String userNameOrEmail, String password) {
        User user = userRepository.findByUserName(userNameOrEmail)
                .or(() -> userRepository.findByEmail(userNameOrEmail))
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Şifre hatalı!");
        }

        // JWT üret ve dön
        return jwtService.generateToken(user.getUserName());
    }
}