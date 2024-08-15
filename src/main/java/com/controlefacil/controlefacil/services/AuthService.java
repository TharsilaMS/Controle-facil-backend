package com.controlefacil.controlefacil.services;

import com.controlefacil.controlefacil.models.User;
import com.controlefacil.controlefacil.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String authenticate(String email, String rawPassword) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                // Gera e retorna o token JWT (implementação de exemplo)
                return "dummy-jwt-token";
            }
        }
        throw new Exception("Invalid credentials");
    }
}