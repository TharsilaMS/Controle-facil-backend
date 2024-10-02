package com.controlefacil.controlefacil.controller;


import com.controlefacil.controlefacil.config.TokenService;
import com.controlefacil.controlefacil.dto.LoginRequestDTO;
import com.controlefacil.controlefacil.dto.RegisterRequestDTO;
import com.controlefacil.controlefacil.dto.ResponseDTO;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO body){
        Usuario usuario = this.repository.findByEmail(body.getEmail()).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        if(passwordEncoder.matches(body.getSenha(), usuario.getSenha())) {
            String token = this.tokenService.generateToken(usuario);
            return ResponseEntity.ok(new ResponseDTO(usuario.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}