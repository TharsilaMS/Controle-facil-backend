package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.UsuarioDTO;
import com.controlefacil.controlefacil.model.FaixaSalarial;
import com.controlefacil.controlefacil.model.Genero;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepare mock Usuario and UsuarioDTO
        UUID userId = UUID.randomUUID();
        usuario = new Usuario(userId, "John Doe", "john.doe@example.com", "password", LocalDateTime.now(),
                Genero.MASCULINO, LocalDate.of(1990, 1, 1), "IT", FaixaSalarial.DE_2K_A_5K);
        usuarioDTO = new UsuarioDTO(userId, "John Doe", "john.doe@example.com", "password", Genero.MASCULINO,
                LocalDate.of(1990, 1, 1), "IT", FaixaSalarial.DE_2K_A_5K);
    }

    @Test
    public void testGetAllUsuarios() {
        when(usuarioService.getAllUsuarios()).thenReturn(List.of(usuario));

        List<UsuarioDTO> response = usuarioController.getAllUsuarios();

        assertEquals(1, response.size());
        assertEquals(usuarioDTO, response.get(0));
    }

    @Test
    public void testGetUsuarioById_Success() {
        when(usuarioService.getUsuarioById(usuario.getIdUsuario())).thenReturn(Optional.of(usuario));

        ResponseEntity<UsuarioDTO> response = usuarioController.getUsuarioById(usuario.getIdUsuario());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioDTO, response.getBody());
    }

    @Test
    public void testGetUsuarioById_NotFound() {
        when(usuarioService.getUsuarioById(usuario.getIdUsuario())).thenReturn(Optional.empty());

        ResponseEntity<UsuarioDTO> response = usuarioController.getUsuarioById(usuario.getIdUsuario());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateUsuario() {
        when(usuarioService.createUsuario(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<UsuarioDTO> response = usuarioController.createUsuario(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(usuarioDTO, response.getBody());
    }

    @Test
    public void testUpdateUsuario_Success() {
        when(usuarioService.updateUsuario(eq(usuario.getIdUsuario()), any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<UsuarioDTO> response = usuarioController.updateUsuario(usuario.getIdUsuario(), usuarioDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioDTO, response.getBody());
    }

    @Test
    public void testUpdateUsuario_NotFound() {
        when(usuarioService.updateUsuario(eq(usuario.getIdUsuario()), any(Usuario.class))).thenReturn(null);

        ResponseEntity<UsuarioDTO> response = usuarioController.updateUsuario(usuario.getIdUsuario(), usuarioDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUsuario() {
        doNothing().when(usuarioService).deleteUsuario(usuario.getIdUsuario());

        ResponseEntity<Void> response = usuarioController.deleteUsuario(usuario.getIdUsuario());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
