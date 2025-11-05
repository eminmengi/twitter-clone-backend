package com.workintech.backend.twitter_clone.service;


import com.workintech.backend.twitter_clone.dto.UserResponse;
import com.workintech.backend.twitter_clone.entity.User;
import com.workintech.backend.twitter_clone.exception.ApiException;
import com.workintech.backend.twitter_clone.exception.UnauthorizedActionException;
import com.workintech.backend.twitter_clone.exception.UserNotFoundException;
import com.workintech.backend.twitter_clone.repository.UserRepository;
import com.workintech.backend.twitter_clone.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Auth servisinin register ve login iş mantığı testleri.
 */
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserName("eminm");
        user.setEmail("emin@example.com");
        user.setPassword("123456"); // raw password (encoder mocklanacak)
    }

    @Test
    void register_shouldSave_whenUnique() {
        // kullanıcı adı & email boş
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("emin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("ENCODED");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse res = authService.register(user);

        assertEquals("eminm", res.getUserName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_shouldThrow_whenUserNameExists() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(new User()));
        assertThrows(ApiException.class, () -> authService.register(user));
    }

    @Test
    void register_shouldThrow_whenEmailExists() {
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("emin@example.com")).thenReturn(Optional.of(new User()));
        assertThrows(ApiException.class, () -> authService.register(user));
    }

    @Test
    void login_shouldReturnToken_whenCredsOk() {
        User db = new User();
        db.setUserName("eminm");
        db.setEmail("emin@example.com");
        db.setPassword("ENCODED");

        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(db));
        when(passwordEncoder.matches("123456", "ENCODED")).thenReturn(true);
        when(jwtService.generateToken("eminm")).thenReturn("token-123");

        String token = authService.login("eminm", "123456");
        assertEquals("token-123", token);
    }

    @Test
    void login_shouldThrow_whenUserNotFound() {
        when(userRepository.findByUserName("x")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("x")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> authService.login("x", "p"));
    }

    @Test
    void login_shouldThrow_whenPasswordWrong() {
        User db = new User();
        db.setUserName("eminm");
        db.setPassword("ENCODED");
        when(userRepository.findByUserName("eminm")).thenReturn(Optional.of(db));
        when(passwordEncoder.matches("wrong", "ENCODED")).thenReturn(false);

        assertThrows(UnauthorizedActionException.class, () -> authService.login("eminm", "wrong"));
    }
}
