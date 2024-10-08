package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.RendaDTO;
import com.controlefacil.controlefacil.model.*;
import com.controlefacil.controlefacil.service.RendaService;
import com.controlefacil.controlefacil.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RendaControllerTest {

    @InjectMocks
    private RendaController rendaController;

    @Mock
    private RendaService rendaService;

    @Mock
    private UsuarioService usuarioService;

    private UUID usuarioId;
    private UUID rendaId;
    private Renda renda;
    private Usuario usuario; // Declare Usuario

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioId = UUID.randomUUID();
        rendaId = UUID.randomUUID();

        // Create a valid Usuario instance
        usuario = new Usuario(usuarioId, "Nome do Usuário", "email@example.com", "senha", LocalDateTime.now(),
                Genero.MASCULINO, LocalDate.of(1990, 1, 1), "Tecnologia", FaixaSalarial.DE_2K_A_5K);

        // Initialize Renda with a valid Usuario and LocalDate
        renda = new Renda(rendaId, usuario, "Descrição", BigDecimal.valueOf(1000), LocalDate.now(), TipoRenda.SALARIO);
    }

    @Test
    void testGetAllRendas() {
        List<Renda> rendas = new ArrayList<>();
        rendas.add(renda);
        when(rendaService.getAllRendas()).thenReturn(rendas);

        List<RendaDTO> result = rendaController.getAllRendas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Descrição", result.get(0).getDescricao());
    }

    @Test
    void testGetRendaById_Found() {
        when(rendaService.getRendaById(rendaId)).thenReturn(Optional.of(renda));

        ResponseEntity<RendaDTO> response = rendaController.getRendaById(rendaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Descrição", response.getBody().getDescricao());
    }

    @Test
    void testGetRendaById_NotFound() {
        when(rendaService.getRendaById(rendaId)).thenReturn(Optional.empty());

        ResponseEntity<RendaDTO> response = rendaController.getRendaById(rendaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetRendasByUsuarioId() {
        List<Renda> rendas = new ArrayList<>();
        rendas.add(renda);
        when(rendaService.getRendasByUsuarioId(usuarioId)).thenReturn(rendas);

        ResponseEntity<List<RendaDTO>> response = rendaController.getRendasByUsuarioId(usuarioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCreateRenda_Success() {
        RendaDTO rendaDTO = new RendaDTO(rendaId, usuarioId, "Nova Renda", BigDecimal.valueOf(1500), LocalDate.now(), TipoRenda.FREELANCE);
        Renda novaRenda = new Renda(rendaId, usuario, rendaDTO.getDescricao(), rendaDTO.getValor(), rendaDTO.getData(), rendaDTO.getTipo());

        when(rendaService.saveRenda(any(Renda.class))).thenReturn(novaRenda);

        ResponseEntity<RendaDTO> response = rendaController.createRenda(rendaDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Nova Renda", response.getBody().getDescricao());
    }

    @Test
    void testCreateRenda_BadRequest() {
        RendaDTO rendaDTO = new RendaDTO(null, null, "Nova Renda", BigDecimal.valueOf(1500), LocalDate.now(), TipoRenda.FREELANCE);

        ResponseEntity<RendaDTO> response = rendaController.createRenda(rendaDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateRenda_Success() {
        RendaDTO rendaDTO = new RendaDTO(rendaId, usuarioId, "Renda Atualizada", BigDecimal.valueOf(2000), LocalDate.now(), TipoRenda.INVESTIMENTO);
        when(rendaService.getRendaById(rendaId)).thenReturn(Optional.of(renda));
        when(rendaService.saveRenda(any(Renda.class))).thenReturn(renda);

        ResponseEntity<RendaDTO> response = rendaController.updateRenda(rendaId, rendaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Renda Atualizada", response.getBody().getDescricao());
    }

    @Test
    void testUpdateRenda_NotFound() {
        RendaDTO rendaDTO = new RendaDTO(rendaId, usuarioId, "Renda Atualizada", BigDecimal.valueOf(2000), LocalDate.now(), TipoRenda.INVESTIMENTO);
        when(rendaService.getRendaById(rendaId)).thenReturn(Optional.empty());

        ResponseEntity<RendaDTO> response = rendaController.updateRenda(rendaId, rendaDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteRenda_Success() {
        when(rendaService.getRendaById(rendaId)).thenReturn(Optional.of(renda));

        ResponseEntity<Void> response = rendaController.deleteRenda(rendaId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(rendaService, times(1)).deleteRenda(rendaId);
    }

    @Test
    void testDeleteRenda_NotFound() {
        when(rendaService.getRendaById(rendaId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = rendaController.deleteRenda(rendaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(rendaService, never()).deleteRenda(any());
    }
}
