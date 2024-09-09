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

import static org.mockito.Mockito.*;

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
    public void testGetAllCategorias() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        CategoriaDespesa categoria1 = new CategoriaDespesa(id1, "Alimentação");
        CategoriaDespesa categoria2 = new CategoriaDespesa(id2, "Transporte");

        when(service.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        ResponseEntity<List<CategoriaDespesaDTO>> response = controller.getAllCategorias();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetCategoriaByIdFound() {
        UUID id = UUID.randomUUID();
        CategoriaDespesa categoria = new CategoriaDespesa(id, "Alimentação");

        when(service.findById(any(UUID.class))).thenReturn(Optional.of(categoria));

        ResponseEntity<CategoriaDespesaDTO> response = controller.getCategoriaById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alimentação", response.getBody().getNome());
    }

    @Test
    public void testGetCategoriaByIdNotFound() {
        when(service.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<CategoriaDespesaDTO> response = controller.getCategoriaById(UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateCategoria() {
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação");
        CategoriaDespesa categoria = new CategoriaDespesa(null, "Alimentação");
        UUID id = UUID.randomUUID();
        CategoriaDespesa createdCategoria = new CategoriaDespesa(id, "Alimentação");

        when(service.save(any(CategoriaDespesa.class))).thenReturn(createdCategoria);

        ResponseEntity<CategoriaDespesaDTO> response = controller.createCategoria(categoriaDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Alimentação", response.getBody().getNome());
    }

    @Test
    public void testUpdateCategoria() {
        UUID id = UUID.randomUUID();
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação Atualizada");
        CategoriaDespesa categoria = new CategoriaDespesa(id, "Alimentação Atualizada");
        CategoriaDespesa updatedCategoria = new CategoriaDespesa(id, "Alimentação Atualizada");

        when(service.findById(any(UUID.class))).thenReturn(Optional.of(categoria));
        when(service.save(any(CategoriaDespesa.class))).thenReturn(updatedCategoria);

        ResponseEntity<CategoriaDespesaDTO> response = controller.updateCategoria(id, categoriaDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alimentação Atualizada", response.getBody().getNome());
    }

    @Test
    public void testUpdateCategoriaNotFound() {
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação Atualizada");

        when(service.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<CategoriaDespesaDTO> response = controller.updateCategoria(UUID.randomUUID(), categoriaDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
