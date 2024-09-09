package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.DespesaDTO;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Tipo;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.DespesaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class DespesaControllerTest {

    @InjectMocks
    private DespesaController controller;

    @Mock
    private DespesaService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDespesas() {
        UUID usuarioId1 = UUID.randomUUID();
        UUID usuarioId2 = UUID.randomUUID();

        Despesa despesa1 = new Despesa(UUID.randomUUID(), new Usuario(usuarioId1), "Descrição 1", BigDecimal.valueOf(100), new CategoriaDespesa(UUID.randomUUID()), Tipo.FIXA, LocalDate.now());
        Despesa despesa2 = new Despesa(UUID.randomUUID(), new Usuario(usuarioId2), "Descrição 2", BigDecimal.valueOf(200), new CategoriaDespesa(UUID.randomUUID()), Tipo.VARIAVEL, LocalDate.now());

        when(service.getAllDespesas()).thenReturn(Arrays.asList(despesa1, despesa2));

        ResponseEntity<List<DespesaDTO>> response = controller.getAllDespesas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetDespesaByIdFound() {
        UUID usuarioId = UUID.randomUUID();
        Despesa despesa = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Descrição 1", BigDecimal.valueOf(100), new CategoriaDespesa(UUID.randomUUID()), Tipo.FIXA, LocalDate.now());

        when(service.getDespesaById(any(UUID.class))).thenReturn(Optional.of(despesa));

        ResponseEntity<DespesaDTO> response = controller.getDespesaById(UUID.randomUUID());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Descrição 1", response.getBody().getDescricao());
    }

    @Test
    public void testGetDespesaByIdNotFound() {
        when(service.getDespesaById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<DespesaDTO> response = controller.getDespesaById(UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateDespesa() {
        UUID usuarioId = UUID.randomUUID();
        DespesaDTO despesaDTO = new DespesaDTO(null, usuarioId, "Descrição", BigDecimal.valueOf(100), UUID.randomUUID(), Tipo.FIXA, LocalDate.now());
        Despesa despesa = new Despesa(null, new Usuario(usuarioId), "Descrição", BigDecimal.valueOf(100), new CategoriaDespesa(UUID.randomUUID()), Tipo.FIXA, LocalDate.now());
        Despesa savedDespesa = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Descrição", BigDecimal.valueOf(100), new CategoriaDespesa(UUID.randomUUID()), Tipo.FIXA, LocalDate.now());

        when(service.saveDespesa(any(Despesa.class))).thenReturn(savedDespesa);

        ResponseEntity<DespesaDTO> response = controller.createDespesa(despesaDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Descrição", response.getBody().getDescricao());
    }

    @Test
    public void testUpdateDespesa() {
        UUID usuarioId = UUID.randomUUID();
        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), usuarioId, "Descrição Atualizada", BigDecimal.valueOf(150), UUID.randomUUID(), Tipo.VARIAVEL, LocalDate.now());
        Despesa despesa = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Descrição Atualizada", BigDecimal.valueOf(150), new CategoriaDespesa(UUID.randomUUID()), Tipo.VARIAVEL, LocalDate.now());
        Despesa updatedDespesa = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Descrição Atualizada", BigDecimal.valueOf(150), new CategoriaDespesa(UUID.randomUUID()), Tipo.VARIAVEL, LocalDate.now());

        when(service.getDespesaById(any(UUID.class))).thenReturn(Optional.of(despesa));
        when(service.saveDespesa(any(Despesa.class))).thenReturn(updatedDespesa);

        ResponseEntity<DespesaDTO> response = controller.updateDespesa(UUID.randomUUID(), despesaDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Descrição Atualizada", response.getBody().getDescricao());
    }

    @Test
    public void testUpdateDespesaNotFound() {
        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), UUID.randomUUID(), "Descrição Atualizada", BigDecimal.valueOf(150), UUID.randomUUID(), Tipo.VARIAVEL, LocalDate.now());

        when(service.getDespesaById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<DespesaDTO> response = controller.updateDespesa(UUID.randomUUID(), despesaDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteDespesa() {
        when(service.getDespesaById(any(UUID.class))).thenReturn(Optional.of(new Despesa()));

        ResponseEntity<Void> response = controller.deleteDespesa(UUID.randomUUID());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteDespesaNotFound() {
        when(service.getDespesaById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<Void> response = controller.deleteDespesa(UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetDespesasByUsuarioId() {
        UUID usuarioId = UUID.randomUUID();
        Despesa despesa1 = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Descrição 1", BigDecimal.valueOf(100), new CategoriaDespesa(UUID.randomUUID()), Tipo.FIXA, LocalDate.now());
        Despesa despesa2 = new Despesa(UUID.randomUUID(), new Usuario(usuarioId), "Descrição 2", BigDecimal.valueOf(200), new CategoriaDespesa(UUID.randomUUID()), Tipo.VARIAVEL, LocalDate.now());

        when(service.getDespesasByUsuarioId(any(UUID.class))).thenReturn(Arrays.asList(despesa1, despesa2));

        ResponseEntity<List<DespesaDTO>> response = controller.getDespesasByUsuarioId(usuarioId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}
