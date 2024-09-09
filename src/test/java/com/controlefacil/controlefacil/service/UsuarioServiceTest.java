package com.controlefacil.controlefacil.service;

import static org.junit.jupiter.api.Assertions.*;
import com.controlefacil.controlefacil.model.Saldo;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.SaldoRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SaldoRepository saldoRepository;

    private Usuario usuario;
    private Saldo saldo;
    private UUID usuarioId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioId = UUID.randomUUID();

        usuario = new Usuario();
        usuario.setIdUsuario(usuarioId);
        usuario.setNome("Usuario Teste");

        saldo = new Saldo();
        saldo.setUsuario(usuario);
        saldo.setSaldo(BigDecimal.ZERO);
        saldo.setData(LocalDate.now());
        saldo.setDescricao("Saldo inicial");
    }

    @Test
    public void testGetAllUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        assertEquals(1, usuarioService.getAllUsuarios().size());
    }

    @Test
    public void testGetUsuarioById() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        assertEquals(usuario, usuarioService.getUsuarioById(usuarioId).get());
    }

    @Test
    public void testCreateUsuario() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(saldoRepository.save(any(Saldo.class))).thenReturn(saldo);

        Usuario newUsuario = usuarioService.createUsuario(usuario);

        assertNotNull(newUsuario);
        assertEquals(usuario.getIdUsuario(), newUsuario.getIdUsuario());
        verify(saldoRepository, times(1)).save(any(Saldo.class));
    }

    @Test
    public void testUpdateUsuario() {
        when(usuarioRepository.existsById(usuarioId)).thenReturn(true);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario updatedUsuario = usuarioService.updateUsuario(usuarioId, usuario);
        assertNotNull(updatedUsuario);
        assertEquals(usuario.getIdUsuario(), updatedUsuario.getIdUsuario());
    }

    @Test
    public void testUpdateUsuario_NotFound() {
        when(usuarioRepository.existsById(usuarioId)).thenReturn(false);

        Usuario updatedUsuario = usuarioService.updateUsuario(usuarioId, usuario);
        assertNull(updatedUsuario);
    }

    @Test
    public void testDeleteUsuario() {
        doNothing().when(usuarioRepository).deleteById(usuarioId);
        usuarioService.deleteUsuario(usuarioId);
        verify(usuarioRepository, times(1)).deleteById(usuarioId);
    }
}
