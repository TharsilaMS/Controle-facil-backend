package com.controlefacil.controlefacil.services;

import com.controlefacil.controlefacil.models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.controlefacil.controlefacil.repositories.UserRepository;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testAuthenticate_ValidCredentials_ReturnsToken() throws Exception {
        String email = "test@example.com";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        String token = authService.authenticate(email, rawPassword);

        assertEquals("dummy-jwt-token", token);
    }

    @Test
    public void testAuthenticate_InvalidEmail_ThrowsException() {
        String email = "test@example.com";
        String rawPassword = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            authService.authenticate(email, rawPassword);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    public void testAuthenticate_InvalidPassword_ThrowsException() throws Exception {
        String email = "test@example.com";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            authService.authenticate(email, rawPassword);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }
}

