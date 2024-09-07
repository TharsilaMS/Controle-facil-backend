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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        CategoriaDespesa categoria1 = new CategoriaDespesa(1L, "Alimentação");
        CategoriaDespesa categoria2 = new CategoriaDespesa(2L, "Transporte");

        when(service.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        ResponseEntity<List<CategoriaDespesaDTO>> response = controller.getAllCategorias();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetCategoriaByIdFound() {
        CategoriaDespesa categoria = new CategoriaDespesa(1L, "Alimentação");

        when(service.findById(anyLong())).thenReturn(Optional.of(categoria));

        ResponseEntity<CategoriaDespesaDTO> response = controller.getCategoriaById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alimentação", response.getBody().getNome());
    }

    @Test
    public void testGetCategoriaByIdNotFound() {
        when(service.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<CategoriaDespesaDTO> response = controller.getCategoriaById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateCategoria() {
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação");
        CategoriaDespesa categoria = new CategoriaDespesa(null, "Alimentação");
        CategoriaDespesa createdCategoria = new CategoriaDespesa(1L, "Alimentação");

        when(service.save(any(CategoriaDespesa.class))).thenReturn(createdCategoria);

        ResponseEntity<CategoriaDespesaDTO> response = controller.createCategoria(categoriaDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Alimentação", response.getBody().getNome());
    }

    @Test
    public void testUpdateCategoria() {
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação Atualizada");
        CategoriaDespesa categoria = new CategoriaDespesa(1L, "Alimentação Atualizada");
        CategoriaDespesa updatedCategoria = new CategoriaDespesa(1L, "Alimentação Atualizada");

        when(service.findById(anyLong())).thenReturn(Optional.of(categoria));
        when(service.save(any(CategoriaDespesa.class))).thenReturn(updatedCategoria);

        ResponseEntity<CategoriaDespesaDTO> response = controller.updateCategoria(1L, categoriaDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alimentação Atualizada", response.getBody().getNome());
    }

    @Test
    public void testUpdateCategoriaNotFound() {
        CategoriaDespesaDTO categoriaDTO = new CategoriaDespesaDTO(null, "Alimentação Atualizada");

        when(service.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<CategoriaDespesaDTO> response = controller.updateCategoria(1L, categoriaDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
