package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DespesaServiceTest {

    @InjectMocks
    private DespesaService despesaService;

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CategoriaDespesaService categoriaDespesaService;

    private Despesa despesa;
    private Usuario usuario;
    private CategoriaDespesa categoriaDespesa;
    private UUID usuarioId;
    private UUID despesaId;
    private UUID categoriaDespesaId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioId = UUID.randomUUID();
        despesaId = UUID.randomUUID();
        categoriaDespesaId = UUID.randomUUID();

        usuario = new Usuario();
        usuario.setIdUsuario(usuarioId);

        categoriaDespesa = new CategoriaDespesa();
        categoriaDespesa.setId(categoriaDespesaId);
        categoriaDespesa.setNome("Categoria Teste");

        despesa = new Despesa();
        despesa.setId(despesaId);
        despesa.setDescricao("Despesa Teste");
        despesa.setValor(BigDecimal.valueOf(100));
        despesa.setData(LocalDate.now());
        despesa.setUsuario(usuario);
        despesa.setCategoriaDespesa(categoriaDespesa);
    }

    @Test
    public void testGetAllDespesas() {
        when(despesaRepository.findAll()).thenReturn(List.of(despesa));
        List<Despesa> despesas = despesaService.getAllDespesas();
        assertEquals(1, despesas.size());
        assertEquals(despesa, despesas.get(0));
    }

    @Test
    public void testGetDespesaById() {
        when(despesaRepository.findById(despesaId)).thenReturn(Optional.of(despesa));
        Optional<Despesa> result = despesaService.getDespesaById(despesaId);
        assertTrue(result.isPresent());
        assertEquals(despesa, result.get());
    }

    @Test
    public void testSaveDespesa() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(categoriaDespesaService.findByNome(despesa.getCategoriaDespesa().getNome()))
                .thenReturn(Optional.of(categoriaDespesa));
        when(despesaRepository.save(despesa)).thenReturn(despesa);

        Despesa savedDespesa = despesaService.saveDespesa(despesa);
        assertNotNull(savedDespesa);
        assertEquals(despesa, savedDespesa);
    }

    @Test
    public void testSaveDespesa_UserNotFound() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> despesaService.saveDespesa(despesa));
    }

    @Test
    public void testSaveDespesa_CategoriaNotFound() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(categoriaDespesaService.findByNome(despesa.getCategoriaDespesa().getNome()))
                .thenReturn(Optional.empty());
        when(categoriaDespesaService.save(any(CategoriaDespesa.class)))
                .thenReturn(categoriaDespesa);
        when(despesaRepository.save(despesa)).thenReturn(despesa);

        Despesa savedDespesa = despesaService.saveDespesa(despesa);
        assertNotNull(savedDespesa);
        assertEquals(despesa, savedDespesa);
    }

    @Test
    public void testUpdateDespesa() {
        Despesa updatedDespesa = new Despesa();
        updatedDespesa.setId(despesaId);
        updatedDespesa.setDescricao("Despesa Atualizada");
        updatedDespesa.setValor(BigDecimal.valueOf(150));
        updatedDespesa.setData(LocalDate.now());
        updatedDespesa.setUsuario(usuario);
        updatedDespesa.setCategoriaDespesa(categoriaDespesa);

        when(despesaRepository.findById(despesaId)).thenReturn(Optional.of(despesa));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(categoriaDespesaService.findByNome(updatedDespesa.getCategoriaDespesa().getNome()))
                .thenReturn(Optional.of(categoriaDespesa));
        when(despesaRepository.save(updatedDespesa)).thenReturn(updatedDespesa);

        Despesa result = despesaService.updateDespesa(despesaId, updatedDespesa);
        assertEquals("Despesa Atualizada", result.getDescricao());
    }

    @Test
    public void testUpdateDespesa_NotFound() {
        when(despesaRepository.findById(despesaId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> despesaService.updateDespesa(despesaId, despesa));
    }

    @Test
    public void testDeleteDespesa() {
        when(despesaRepository.findById(despesaId)).thenReturn(Optional.of(despesa));
        doNothing().when(despesaRepository).delete(despesa);

        despesaService.deleteDespesa(despesaId);
        verify(despesaRepository, times(1)).delete(despesa);
    }

    @Test
    public void testDeleteDespesa_NotFound() {
        when(despesaRepository.findById(despesaId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> despesaService.deleteDespesa(despesaId));
    }

    @Test
    public void testGetDespesasByUsuarioId() {
        when(despesaRepository.findByUsuario_IdUsuario(usuarioId)).thenReturn(List.of(despesa));
        List<Despesa> despesas = despesaService.getDespesasByUsuarioId(usuarioId);
        assertEquals(1, despesas.size());
        assertEquals(despesa, despesas.get(0));
    }
}
