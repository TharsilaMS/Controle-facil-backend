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
import java.util.UUID;

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
    private UUID rendaId;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        usuario = new Usuario();
        usuario.setIdUsuario(usuarioId);

        rendaId = UUID.randomUUID();
        renda = new Renda();
        renda.setId(rendaId);
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
        when(rendaRepository.findById(rendaId)).thenReturn(Optional.of(renda));

        Optional<Renda> foundRenda = rendaService.getRendaById(rendaId);
        assertTrue(foundRenda.isPresent());
        assertEquals(renda.getDescricao(), foundRenda.get().getDescricao());

        verify(rendaRepository, times(1)).findById(rendaId);
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
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(rendaRepository.save(renda)).thenReturn(renda);

        Renda savedRenda = rendaService.saveRenda(renda);

        assertNotNull(savedRenda);
        assertEquals("Salário", savedRenda.getDescricao());
        verify(rendaRepository, times(1)).save(renda);
    }

    @Test
    void testSaveRendaWithNonExistentUsuarioThrowsException() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            rendaService.saveRenda(renda);
        });

        assertEquals("Usuário não encontrado com o id " + usuarioId, exception.getMessage());
    }

    @Test
    void testUpdateRenda() {
        Renda rendaDetails = new Renda();
        rendaDetails.setDescricao("Novo Salário");
        rendaDetails.setValor(new BigDecimal("6000.00"));
        rendaDetails.setData(LocalDate.now());

        when(rendaRepository.findById(rendaId)).thenReturn(Optional.of(renda));
        when(rendaRepository.save(renda)).thenReturn(renda);

        Renda updatedRenda = rendaService.updateRenda(rendaId, rendaDetails);

        assertEquals("Novo Salário", updatedRenda.getDescricao());
        assertEquals(new BigDecimal("6000.00"), updatedRenda.getValor());
        verify(rendaRepository, times(1)).save(renda);
    }

    @Test
    void testDeleteRenda() {
        when(rendaRepository.findById(rendaId)).thenReturn(Optional.of(renda));

        rendaService.deleteRenda(rendaId);

        verify(rendaRepository, times(1)).delete(renda);
    }

    @Test
    void testDeleteRendaWithNonExistentIdThrowsException() {
        when(rendaRepository.findById(rendaId)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            rendaService.deleteRenda(rendaId);
        });

        assertEquals("Renda não encontrada com o id " + rendaId, exception.getMessage());
    }
}
