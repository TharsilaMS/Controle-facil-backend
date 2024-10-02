package com.controlefacil.controlefacil.config;

import java.util.Date;
import com.controlefacil.controlefacil.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null) {
            throw new IllegalArgumentException("Usuário ou email não podem ser nulos");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while authenticating");
        }
    }

    public String validateToken(String token) {
        // Permitir que um token nulo não cause uma exceção
        if (token == null) {
            return null; // Retorna nulo se o token for nulo
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null; // Retorna nulo se a verificação falhar
        }
    }

    private Date generateExpirationDate() {
        return Date.from(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")));
    }
}
