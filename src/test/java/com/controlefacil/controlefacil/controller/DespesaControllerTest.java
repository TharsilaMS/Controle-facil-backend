package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.DespesaDTO;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Tipo;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.CategoriaDespesaService;
import com.controlefacil.controlefacil.service.DespesaService;
import com.controlefacil.controlefacil.service.UsuarioService;
import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DespesaControllerTest {

    @InjectMocks
    private DespesaController controller;

    @Mock
    private DespesaService despesaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CategoriaDespesaService categoriaDespesaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDespesas() {
        UUID usuarioId = UUID.randomUUID();
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setNome("Alimentação");

        Despesa despesa1 = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Supermercado", BigDecimal.valueOf(200), categoria, Tipo.FIXA, LocalDate.now());
        Despesa despesa2 = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Restaurante", BigDecimal.valueOf(150), categoria, Tipo.FIXA, LocalDate.now());

        when(despesaService.getAllDespesas()).thenReturn(Arrays.asList(despesa1, despesa2));

        ResponseEntity<List<DespesaDTO>> resposta = controller.getAllDespesas();
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(2, resposta.getBody().size());
        assertEquals("Supermercado", resposta.getBody().get(0).getDescricao());
    }

    @Test
    public void testGetDespesaByIdEncontrada() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setNome("Alimentação");

        Despesa despesa = new Despesa(id, new Usuario(usuarioId), "Supermercado", BigDecimal.valueOf(200), categoria, Tipo.FIXA, LocalDate.now());

        when(despesaService.getDespesaById(id)).thenReturn(Optional.of(despesa));

        ResponseEntity<DespesaDTO> resposta = controller.getDespesaById(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Supermercado", resposta.getBody().getDescricao());
    }

    @Test
    public void testGetDespesaByIdNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(despesaService.getDespesaById(id)).thenReturn(Optional.empty());

        ResponseEntity<DespesaDTO> resposta = controller.getDespesaById(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    public void testCreateDespesaComSucesso() {
        UUID usuarioId = UUID.randomUUID();
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setNome("Alimentação");

        DespesaDTO despesaDTO = new DespesaDTO(null, usuarioId, "Supermercado", BigDecimal.valueOf(200), "Alimentação", Tipo.FIXA, LocalDate.now());
        Despesa despesa = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Supermercado", BigDecimal.valueOf(200), categoria, Tipo.FIXA, LocalDate.now());

        when(usuarioService.getUsuarioById(usuarioId)).thenReturn(Optional.of(new Usuario(usuarioId)));
        when(categoriaDespesaService.findByNome("Alimentação")).thenReturn(Optional.of(categoria));
        when(despesaService.saveDespesa(any(Despesa.class))).thenReturn(despesa);

        ResponseEntity<DespesaDTO> resposta = controller.createDespesa(despesaDTO);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("Supermercado", resposta.getBody().getDescricao());
    }

    @Test
    public void testCreateDespesaSemUsuarioId() {
        DespesaDTO despesaDTO = new DespesaDTO(null, null, "Supermercado", BigDecimal.valueOf(200), "Alimentação", Tipo.FIXA, LocalDate.now());

        ResponseEntity<DespesaDTO> resposta = controller.createDespesa(despesaDTO);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertNull(resposta.getBody());
    }



    @Test
    public void testUpdateDespesaComSucesso() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setNome("Alimentação");

        DespesaDTO despesaDTO = new DespesaDTO(null, usuarioId, "Supermercado Atualizado", BigDecimal.valueOf(250), "Alimentação", Tipo.FIXA, LocalDate.now());
        Despesa despesaExistente = new Despesa(id, new Usuario(usuarioId), "Supermercado", BigDecimal.valueOf(200), categoria, Tipo.FIXA, LocalDate.now());
        Despesa despesaAtualizada = new Despesa(id, new Usuario(usuarioId), "Supermercado Atualizado", BigDecimal.valueOf(250), categoria, Tipo.FIXA, LocalDate.now());

        when(despesaService.getDespesaById(id)).thenReturn(Optional.of(despesaExistente));
        when(usuarioService.getUsuarioById(usuarioId)).thenReturn(Optional.of(new Usuario(usuarioId)));
        when(categoriaDespesaService.findByNome("Alimentação")).thenReturn(Optional.of(categoria));
        when(despesaService.updateDespesa(eq(id), any(Despesa.class))).thenReturn(despesaAtualizada);

        ResponseEntity<DespesaDTO> resposta = controller.updateDespesa(id, despesaDTO);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Supermercado Atualizado", resposta.getBody().getDescricao());
    }

    @Test
    public void testUpdateDespesaNaoEncontrada() {
        UUID id = UUID.randomUUID();
        DespesaDTO despesaDTO = new DespesaDTO(null, UUID.randomUUID(), "Supermercado Atualizado", BigDecimal.valueOf(250), "Alimentação", Tipo.FIXA, LocalDate.now());

        when(despesaService.getDespesaById(id)).thenReturn(Optional.empty());

        ResponseEntity<DespesaDTO> resposta = controller.updateDespesa(id, despesaDTO);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    public void testDeleteDespesaComSucesso() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setNome("Alimentação");

        Despesa despesaExistente = new Despesa(id, new Usuario(usuarioId), "Supermercado", BigDecimal.valueOf(200), categoria, Tipo.FIXA, LocalDate.now());

        when(despesaService.getDespesaById(id)).thenReturn(Optional.of(despesaExistente));

        ResponseEntity<Void> resposta = controller.deleteDespesa(id);
        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        verify(despesaService, times(1)).deleteDespesa(id);
    }

    @Test
    public void testDeleteDespesaNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(despesaService.getDespesaById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> resposta = controller.deleteDespesa(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}
