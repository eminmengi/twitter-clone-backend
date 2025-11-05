package com.workintech.backend.twitter_clone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Şifreleri hash'lemek için PasswordEncoder bean'i
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt güçlü bir hashing algoritmasıdır, şifreler düz yazı olarak tutulmaz.
        return new BCryptPasswordEncoder();
    }

    // Güvenlik filtre zinciri: hangi endpoint'lere kimin erişeceğini belirler.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Şu an test aşamasında olduğumuz için tüm istekler serbest.
        // JWT eklediğimizde burayı güncelleyeceğiz.
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // herkes erişebilir
                );

        return http.build();
    }
}
