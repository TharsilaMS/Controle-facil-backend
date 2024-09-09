package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.CategoriaDespesaDTO;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.service.CategoriaDespesaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringJUnitConfig
public class CategoriaDespesaControllerTest {

    @InjectMocks
    private CategoriaDespesaController controller;

    @Mock
    private CategoriaDespesaService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuscarTodasCategorias() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        CategoriaDespesa categoria1 = new CategoriaDespesa(id1, "Alimentação");
        CategoriaDespesa categoria2 = new CategoriaDespesa(id2, "Transporte");

        when(service.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        ResponseEntity<List<CategoriaDespesaDTO>> resposta = controller.getAllCategorias();
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(2, resposta.getBody().size());
        assertEquals("Alimentação", resposta.getBody().get(0).getNome());
        assertEquals("Transporte", resposta.getBody().get(1).getNome());
    }

    @Test
    public void testBuscarCategoriaPorIdEncontrado() {
        UUID id = UUID.randomUUID();
        CategoriaDespesa categoria = new CategoriaDespesa(id, "Alimentação");

        when(service.findById(id)).thenReturn(Optional.of(categoria));

        ResponseEntity<CategoriaDespesaDTO> resposta = controller.getCategoriaById(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Alimentação", resposta.getBody().getNome());
    }

    @Test
    public void testBuscarCategoriaPorIdNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<CategoriaDespesaDTO> resposta = controller.getCategoriaById(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    public void testCriarCategoriaComSucesso() {
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação");
        CategoriaDespesa categoria = new CategoriaDespesa(null, "Alimentação");
        UUID id = UUID.randomUUID();
        CategoriaDespesa categoriaCriada = new CategoriaDespesa(id, "Alimentação");

        when(service.save(any(CategoriaDespesa.class))).thenReturn(categoriaCriada);

        ResponseEntity<CategoriaDespesaDTO> resposta = controller.createCategoria(categoriaDTO);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("Alimentação", resposta.getBody().getNome());
        assertEquals(id, resposta.getBody().getId());
    }

    @Test
    public void testCriarCategoriaComFalha() {
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação");

        when(service.save(any(CategoriaDespesa.class))).thenThrow(new IllegalArgumentException("Erro ao criar categoria"));

        ResponseEntity<CategoriaDespesaDTO> resposta = controller.createCategoria(categoriaDTO);
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertEquals("Erro ao criar categoria", resposta.getBody().getNome());
    }

    @Test
    public void testAtualizarCategoriaComSucesso() {
        UUID id = UUID.randomUUID();
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação Atualizada");
        CategoriaDespesa categoriaExistente = new CategoriaDespesa(id, "Alimentação");
        CategoriaDespesa categoriaAtualizada = new CategoriaDespesa(id, "Alimentação Atualizada");

        when(service.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(service.save(any(CategoriaDespesa.class))).thenReturn(categoriaAtualizada);

        ResponseEntity<CategoriaDespesaDTO> resposta = controller.updateCategoria(id, categoriaDTO);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Alimentação Atualizada", resposta.getBody().getNome());
        assertEquals(id, resposta.getBody().getId());
    }

    @Test
    public void testAtualizarCategoriaNaoEncontrada() {
        UUID id = UUID.randomUUID();
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação Atualizada");

        when(service.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<CategoriaDespesaDTO> resposta = controller.updateCategoria(id, categoriaDTO);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}
