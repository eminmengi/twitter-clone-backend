package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService interface'inin implementasyonu.
 * Kullanıcı kayıt ve giriş işlemlerini gerçekleştirir.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        // Benzersizlik kontrolleri
        userRepository.findByUserName(user.getUserName()).ifPresent(u -> {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor!");
        });

        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Bu e-posta adresi zaten kayıtlı!");
        });

        // Şifreyi hash'le
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Kaydet
        return userRepository.save(user);
    }

    @Override
    public User login(String userNameOrEmail, String password) {
        // Username veya email ile kullanıcıyı bul
        User user = userRepository.findByUserName(userNameOrEmail)
                .or(() -> userRepository.findByEmail(userNameOrEmail))
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Şifre kontrolü
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Şifre hatalı!");
        }

        return user;
    }
}