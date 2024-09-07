package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.RendaRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RendaServiceTest {

    @InjectMocks
    private RendaService rendaService;

    @Mock
    private RendaRepository rendaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Renda renda;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);

        renda = new Renda();
        renda.setId(1L);
        renda.setDescricao("Salário");
        renda.setValor(new BigDecimal("5000.00"));
        renda.setData(LocalDate.now());
        renda.setUsuario(usuario);
    }

    @Test
    void testGetAllRendas() {
        rendaService.getAllRendas();
        verify(rendaRepository, times(1)).findAll();
    }

    @Test
    void testGetRendaById() {
        when(rendaRepository.findById(1L)).thenReturn(Optional.of(renda));

        Optional<Renda> foundRenda = rendaService.getRendaById(1L);
        assertTrue(foundRenda.isPresent());
        assertEquals(renda.getDescricao(), foundRenda.get().getDescricao());

        verify(rendaRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRendaByIdWithNullIdThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rendaService.getRendaById(null);
        });
        assertEquals("O ID da renda não pode ser nulo.", exception.getMessage());
    }

    @Test
    void testSaveRenda() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rendaRepository.save(renda)).thenReturn(renda);

        Renda savedRenda = rendaService.saveRenda(renda);

        assertNotNull(savedRenda);
        assertEquals("Salário", savedRenda.getDescricao());
        verify(rendaRepository, times(1)).save(renda);
    }

    @Test
    void testSaveRendaWithNonExistentUsuarioThrowsException() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            rendaService.saveRenda(renda);
        });

        assertEquals("Usuário não encontrado com o id 1", exception.getMessage());
    }

    @Test
    void testUpdateRenda() {
        Renda rendaDetails = new Renda();
        rendaDetails.setDescricao("Novo Salário");
        rendaDetails.setValor(new BigDecimal("6000.00"));
        rendaDetails.setData(LocalDate.now());

        when(rendaRepository.findById(1L)).thenReturn(Optional.of(renda));
        when(rendaRepository.save(renda)).thenReturn(renda);

        Renda updatedRenda = rendaService.updateRenda(1L, rendaDetails);

        assertEquals("Novo Salário", updatedRenda.getDescricao());
        assertEquals(new BigDecimal("6000.00"), updatedRenda.getValor());
        verify(rendaRepository, times(1)).save(renda);
    }

    @Test
    void testDeleteRenda() {
        when(rendaRepository.findById(1L)).thenReturn(Optional.of(renda));

        rendaService.deleteRenda(1L);

        verify(rendaRepository, times(1)).delete(renda);
    }

    @Test
    void testDeleteRendaWithNonExistentIdThrowsException() {
        when(rendaRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            rendaService.deleteRenda(1L);
        });

        assertEquals("Renda não encontrada com o id 1", exception.getMessage());
    }
}
