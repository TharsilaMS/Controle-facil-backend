package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.DespesaRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DespesaServiceTest {

    @InjectMocks
    private DespesaService despesaService;

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Despesa despesa;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setIdUsuario(1L);

        despesa = new Despesa();
        despesa.setId(1L);
        despesa.setDescricao("Despesa Teste");
        despesa.setValor(BigDecimal.valueOf(100));
        despesa.setData(LocalDate.now());
        despesa.setUsuario(usuario);
    }

    @Test
    public void testGetAllDespesas() {
        when(despesaRepository.findAll()).thenReturn(List.of(despesa));
        assertEquals(1, despesaService.getAllDespesas().size());
    }

    @Test
    public void testGetDespesaById() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));
        assertEquals(despesa, despesaService.getDespesaById(1L).get());
    }

    @Test
    public void testSaveDespesa() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(despesaRepository.save(despesa)).thenReturn(despesa);

        Despesa savedDespesa = despesaService.saveDespesa(despesa);
        assertNotNull(savedDespesa);
        assertEquals(despesa, savedDespesa);
    }

    @Test
    public void testSaveDespesa_UserNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> despesaService.saveDespesa(despesa));
    }

    @Test
    public void testUpdateDespesa() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(despesaRepository.save(despesa)).thenReturn(despesa);

        Despesa updatedDespesa = new Despesa();
        updatedDespesa.setId(1L);
        updatedDespesa.setDescricao("Despesa Atualizada");
        updatedDespesa.setValor(BigDecimal.valueOf(150));
        updatedDespesa.setData(LocalDate.now());
        updatedDespesa.setUsuario(usuario);

        Despesa result = despesaService.updateDespesa(1L, updatedDespesa);
        assertEquals("Despesa Atualizada", result.getDescricao());
    }

    @Test
    public void testUpdateDespesa_NotFound() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> despesaService.updateDespesa(1L, despesa));
    }

    @Test
    public void testDeleteDespesa() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));
        doNothing().when(despesaRepository).delete(despesa);

        despesaService.deleteDespesa(1L);
        verify(despesaRepository, times(1)).delete(despesa);
    }

    @Test
    public void testDeleteDespesa_NotFound() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> despesaService.deleteDespesa(1L));
    }

    @Test
    public void testGetDespesasByUsuarioId() {
        when(despesaRepository.findByUsuario_IdUsuario(1L)).thenReturn(List.of(despesa));
        assertEquals(1, despesaService.getDespesasByUsuarioId(1L).size());
    }
}
