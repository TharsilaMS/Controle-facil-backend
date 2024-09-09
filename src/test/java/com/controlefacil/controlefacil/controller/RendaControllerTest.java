package com.controlefacil.controlefacil.controller;


import com.controlefacil.controlefacil.dto.RendaDTO;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.RendaService;
import com.controlefacil.controlefacil.service.UsuarioService;
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
public class RendaControllerTest {

    @InjectMocks
    private RendaController controller;

    @Mock
    private RendaService rendaService;

    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRendas() {
        UUID usuarioId = UUID.randomUUID();
        Renda renda1 = new Renda(UUID.randomUUID(), new Usuario(usuarioId), "Salário", BigDecimal.valueOf(1000), LocalDate.now());
        Renda renda2 = new Renda(UUID.randomUUID(), new Usuario(usuarioId), "Freelance", BigDecimal.valueOf(500), LocalDate.now());

        when(rendaService.getAllRendas()).thenReturn(Arrays.asList(renda1, renda2));

        List<RendaDTO> resposta = controller.getAllRendas();
        assertEquals(2, resposta.size());
        assertEquals("Salário", resposta.get(0).getDescricao());
        assertEquals("Freelance", resposta.get(1).getDescricao());
    }

    @Test
    public void testGetRendaByIdEncontrada() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        Renda renda = new Renda(id, new Usuario(usuarioId), "Salário", BigDecimal.valueOf(1000), LocalDate.now());

        when(rendaService.getRendaById(id)).thenReturn(Optional.of(renda));

        ResponseEntity<RendaDTO> resposta = controller.getRendaById(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Salário", resposta.getBody().getDescricao());
    }

    @Test
    public void testGetRendaByIdNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(rendaService.getRendaById(id)).thenReturn(Optional.empty());

        ResponseEntity<RendaDTO> resposta = controller.getRendaById(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    public void testCreateRendaComSucesso() {
        UUID usuarioId = UUID.randomUUID();
        RendaDTO rendaDTO = new RendaDTO(null, usuarioId, "Salário", BigDecimal.valueOf(1000), LocalDate.now());
        Renda renda = new Renda(UUID.randomUUID(), new Usuario(usuarioId), "Salário", BigDecimal.valueOf(1000), LocalDate.now());

        when(rendaService.saveRenda(any(Renda.class))).thenReturn(renda);

        ResponseEntity<RendaDTO> resposta = controller.createRenda(rendaDTO);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("Salário", resposta.getBody().getDescricao());
    }

    @Test
    public void testCreateRendaSemUsuarioId() {
        RendaDTO rendaDTO = new RendaDTO(null, null, "Salário", BigDecimal.valueOf(1000), LocalDate.now());

        ResponseEntity<RendaDTO> resposta = controller.createRenda(rendaDTO);
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertEquals(null, resposta.getBody());
    }

    @Test
    public void testUpdateRendaComSucesso() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        RendaDTO rendaDTO = new RendaDTO(null, usuarioId, "Salário Atualizado", BigDecimal.valueOf(1200), LocalDate.now());
        Renda rendaExistente = new Renda(id, new Usuario(usuarioId), "Salário", BigDecimal.valueOf(1000), LocalDate.now());
        Renda rendaAtualizada = new Renda(id, new Usuario(usuarioId), "Salário Atualizado", BigDecimal.valueOf(1200), LocalDate.now());

        when(rendaService.getRendaById(id)).thenReturn(Optional.of(rendaExistente));
        when(rendaService.saveRenda(any(Renda.class))).thenReturn(rendaAtualizada);

        ResponseEntity<RendaDTO> resposta = controller.updateRenda(id, rendaDTO);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Salário Atualizado", resposta.getBody().getDescricao());
    }

    @Test
    public void testUpdateRendaNaoEncontrada() {
        UUID id = UUID.randomUUID();
        RendaDTO rendaDTO = new RendaDTO(null, UUID.randomUUID(), "Salário Atualizado", BigDecimal.valueOf(1200), LocalDate.now());

        when(rendaService.getRendaById(id)).thenReturn(Optional.empty());

        ResponseEntity<RendaDTO> resposta = controller.updateRenda(id, rendaDTO);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    public void testDeleteRendaComSucesso() {
        UUID id = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        Renda rendaExistente = new Renda(id, new Usuario(usuarioId), "Salário", BigDecimal.valueOf(1000), LocalDate.now());

        when(rendaService.getRendaById(id)).thenReturn(Optional.of(rendaExistente));

        ResponseEntity<Void> resposta = controller.deleteRenda(id);
        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        verify(rendaService, times(1)).deleteRenda(id);
    }

    @Test
    public void testDeleteRendaNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(rendaService.getRendaById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> resposta = controller.deleteRenda(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}
