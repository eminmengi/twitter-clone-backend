package com.workintech.backend.twitter_clone.security;

import com.workintech.backend.twitter_clone.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * JWT üretimi ve doğrulama işlemlerini yapar.
 * generateToken() → kullanıcıya yeni token üretir
 * extractUserName() → token içindeki kullanıcı adını çıkarır
 * isTokenValid() → token süresi & imzası geçerli mi kontrol eder
 */
@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationTime;

    // constructor injection: secret ve expiration application.properties'ten gelir
    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long expirationTime
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expirationTime;
    }

    /**
     * Token üretir.
     * @param userName token içine yazılacak subject (kullanıcı adı)
     */
    public String generateToken(String userName) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(userName)             // token içine username koyuyoruz
                .setIssuedAt(now)                 // oluşturulma zamanı
                .setExpiration(expiration)        // bitiş zamanı
                .signWith(signingKey, SignatureAlgorithm.HS256) // imzalama algoritması
                .compact();                       // token string'e dönüştürülür
    }

    /**
     * Token içindeki kullanıcı adını (subject) çözer.
     */
    public String extractUserName(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    /**
     * Token hala geçerli mi?
     */
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token); // eğer süresi dolmuş veya imza hatalıysa exception fırlatır
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Token belirtilen kullanıcıya mı ait? Ve hala geçerli mi?
     */
    public boolean isTokenValid(String token, User user) {
        try {
            final String username = extractUserName(token);
            return (username.equals(user.getUserName()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Token süresi dolmuş mu?
     */
    private boolean isTokenExpired(String token) {
        Date expiration = parseClaims(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Token'ı parse eder (imza ve süresini kontrol eder).
     */
    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }
}