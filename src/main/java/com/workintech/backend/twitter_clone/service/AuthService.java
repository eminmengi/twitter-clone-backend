package com.workintech.backend.twitter_clone.service;

import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Bu servis kullanıcı kayıt (register) ve giriş (login) işlemlerini yönetir.
 * Şifre güvenliğini sağlamak için PasswordEncoder (BCrypt) kullanıyoruz.
 * JWT eklenmeden önce temel kontrol mekanizması burada kurulur.
 */

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //REGISTER
    public User register(User user) {
        // Aynı kullanıcı adı var mı?
        userRepository.findByUserName(user.getUserName()).ifPresent(u -> {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor!");
        });

        // Aynı e-posta var mı?
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Bu e-posta adresi zaten kayıtlı!");
        });

        // Şifreyi hash’le (BCrypt)
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Veritabanına kaydet
        return userRepository.save(user);
    }

    //LOGIN
    public User login(String userNameOrEmail, String password) {
        // Kullanıcıyı username veya email'e göre bul
        User user = userRepository.findByUserName(userNameOrEmail)
                .or(() -> userRepository.findByEmail(userNameOrEmail))
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Şifreyi doğrula (BCrypt hash kontrolü)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Şifre hatalı!");
        }

        // Giriş başarılı → kullanıcı nesnesini döndür
        return user;
    }
}
