package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.PrevisaoGastosDTO;
import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.PrevisaoGastosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PrevisaoGastosControllerTest {

    @InjectMocks
    private PrevisaoGastosController controller;

    @Mock
    private PrevisaoGastosService previsaoGastosService;

    private UUID usuarioId;
    private PrevisaoGastos previsaoGastos;
    private PrevisaoGastosDTO previsaoGastosDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioId = UUID.randomUUID();
        UUID previsaoGastosId = UUID.randomUUID();
        previsaoGastos = new PrevisaoGastos();
        previsaoGastos.setId(previsaoGastosId);
        previsaoGastos.setUsuario(new Usuario(usuarioId));
        previsaoGastos.setLimiteGastos(BigDecimal.valueOf(1000));
        previsaoGastos.setGastosAtuais(BigDecimal.valueOf(200));
        previsaoGastos.setDataRevisao(LocalDate.now());

        previsaoGastosDTO = new PrevisaoGastosDTO(
                previsaoGastosId,
                usuarioId,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(200),
                LocalDate.now()
        );
    }

    @Test
    public void testCreatePrevisaoGastos() {
        when(previsaoGastosService.createPrevisaoGastos(any(PrevisaoGastos.class))).thenReturn(previsaoGastos);

        ResponseEntity<PrevisaoGastosDTO> response = controller.createPrevisaoGastos(previsaoGastosDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(previsaoGastosDTO, response.getBody());
    }

    @Test
    public void testUpdatePrevisaoGastos() {
        when(previsaoGastosService.updatePrevisaoGastos(any(UUID.class), any(PrevisaoGastos.class)))
                .thenReturn(previsaoGastos);

        ResponseEntity<PrevisaoGastosDTO> response = controller.updatePrevisaoGastos(usuarioId, previsaoGastosDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(previsaoGastosDTO, response.getBody());
    }

    @Test
    public void testGetPrevisaoGastos() {
        when(previsaoGastosService.getPrevisaoGastos(any(UUID.class))).thenReturn(previsaoGastos);

        ResponseEntity<PrevisaoGastosDTO> response = controller.getPrevisaoGastos(usuarioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(previsaoGastosDTO, response.getBody());
    }

    @Test
    public void testCreatePrevisaoGastosNotFound() {
        when(previsaoGastosService.createPrevisaoGastos(any(PrevisaoGastos.class)))
                .thenThrow(new RuntimeException("Exception"));
        ResponseEntity<PrevisaoGastosDTO> response = controller.createPrevisaoGastos(previsaoGastosDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testUpdatePrevisaoGastosNotFound() {
        when(previsaoGastosService.updatePrevisaoGastos(any(UUID.class), any(PrevisaoGastos.class)))
                .thenThrow(new RuntimeException("Exception"));

        ResponseEntity<PrevisaoGastosDTO> response = controller.updatePrevisaoGastos(usuarioId, previsaoGastosDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetPrevisaoGastosNotFound() {
        when(previsaoGastosService.getPrevisaoGastos(any(UUID.class)))
                .thenThrow(new RuntimeException("Exception"));

        ResponseEntity<PrevisaoGastosDTO> response = controller.getPrevisaoGastos(usuarioId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
