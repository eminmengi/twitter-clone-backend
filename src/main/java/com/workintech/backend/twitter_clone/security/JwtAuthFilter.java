package com.workintech.backend.twitter_clone.security;

import com.workintech.backend.twitter_clone.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Her gelen HTTP isteğinde Authorization header'ını kontrol eder.
 * Token geçerliyse SecurityContext içine kullanıcı kimliğini yerleştirir.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String userName;

        // Header yoksa diğer filtrelere devam et
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " kısmını at
        token = authHeader.substring(7);
        userName = jwtService.extractUserName(token);

        // Kullanıcı doğrulanmamışsa ve token geçerliyse kimliği context'e koy
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var user = userRepository.findByUserName(userName).orElse(null);

            if (user != null && jwtService.isTokenValid(token)) {
                // Authentication objesini oluştur (authority yok, basit)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user.getUserName(), // principal olarak sadece username koy
                                null,
                                new ArrayList<>()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Context'e ekle
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
