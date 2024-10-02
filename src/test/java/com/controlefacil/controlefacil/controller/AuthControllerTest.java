package com.controlefacil.controlefacil.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.controlefacil.controlefacil.config.TokenService;
import com.controlefacil.controlefacil.dto.LoginRequestDTO;
import com.controlefacil.controlefacil.dto.ResponseDTO;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private Usuario usuario;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setNome("Vilma");
        usuario.setEmail("vilma@ex.com");
        usuario.setSenha("vilma"); // Essa deve ser a senha codificada

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("vilma@ex.com");
        loginRequestDTO.setSenha("vilma"); // A senha aqui deve ser a mesma que a codificada
    }

    @Test
    void testLogin_Success() {
        // Arrange
        when(usuarioRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequestDTO.getSenha(), usuario.getSenha())).thenReturn(true);
        when(tokenService.generateToken(usuario)).thenReturn("token");


        ResponseEntity<ResponseDTO> response = authController.login(loginRequestDTO);


        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Vilma", response.getBody().getNome());
        assertEquals("token", response.getBody().getToken());
    }

    @Test
    void testLogin_UserNotFound() {

        when(usuarioRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> authController.login(loginRequestDTO));

        assertEquals("Usuario não encontrado", exception.getMessage());
    }

    @Test
    void testLogin_InvalidPassword() {
        // Arrange
        when(usuarioRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequestDTO.getSenha(), usuario.getSenha())).thenReturn(false);

        // Act
        ResponseEntity<ResponseDTO> response = authController.login(loginRequestDTO);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }
}
