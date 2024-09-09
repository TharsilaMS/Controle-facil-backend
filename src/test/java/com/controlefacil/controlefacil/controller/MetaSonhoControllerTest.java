package com.controlefacil.controlefacil.controller;


import com.controlefacil.controlefacil.dto.MetaSonhoDTO;
import com.controlefacil.controlefacil.model.MetaSonho;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.MetaSonhoService;
import com.controlefacil.controlefacil.util.ConversorDeData;
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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class MetaSonhoControllerTest {

    @InjectMocks
    private MetaSonhoController controller;

    @Mock
    private MetaSonhoService metaSonhoService;

    private UUID usuarioId;
    private MetaSonho metaSonho;
    private MetaSonhoDTO metaSonhoDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioId = UUID.randomUUID();
        metaSonho = new MetaSonho();
        metaSonho.setId(UUID.randomUUID());
        metaSonho.setTitulo("Title");
        metaSonho.setDescricao("Description");
        metaSonho.setValorAlvo(BigDecimal.valueOf(1000));
        metaSonho.setValorTotal(BigDecimal.valueOf(1000));
        metaSonho.setValorEconomizado(BigDecimal.ZERO);
        metaSonho.setPrazo(LocalDateTime.of(LocalDate.now().plusDays(30), LocalTime.MIDNIGHT));
        metaSonho.setUsuario(new Usuario(usuarioId));

        metaSonhoDTO = new MetaSonhoDTO();
        metaSonhoDTO.setId(metaSonho.getId());
        metaSonhoDTO.setTitulo(metaSonho.getTitulo());
        metaSonhoDTO.setDescricao(metaSonho.getDescricao());
        metaSonhoDTO.setValorAlvo(metaSonho.getValorAlvo());
        metaSonhoDTO.setValorEconomizado(metaSonho.getValorEconomizado());
        metaSonhoDTO.setPrazo(ConversorDeData.formatDate(metaSonho.getPrazo().toLocalDate()));
        metaSonhoDTO.setStatus(metaSonho.getStatus());
        metaSonhoDTO.setUsuarioId(usuarioId);
    }

    @Test
    public void testGetAllMetasSonho() {
        when(metaSonhoService.getAllMetaSonhosByUsuarioId(any(UUID.class)))
                .thenReturn(Arrays.asList(metaSonho));

        ResponseEntity<List<MetaSonhoDTO>> response = controller.getAllMetasSonho(usuarioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(metaSonhoDTO, response.getBody().get(0));
    }

    @Test
    public void testGetMetaSonhoById() {
        when(metaSonhoService.getMetaSonhoById(any(UUID.class)))
                .thenReturn(metaSonho);

        ResponseEntity<MetaSonhoDTO> response = controller.getMetaSonhoById(metaSonho.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(metaSonhoDTO, response.getBody());
    }

    @Test
    public void testGetMetaSonhoByIdNotFound() {
        when(metaSonhoService.getMetaSonhoById(any(UUID.class)))
                .thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<MetaSonhoDTO> response = controller.getMetaSonhoById(UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateMetaSonho() {
        when(metaSonhoService.createMetaSonho(any(MetaSonho.class)))
                .thenReturn(metaSonho);

        ResponseEntity<MetaSonhoDTO> response = controller.createMetaSonho(metaSonhoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(metaSonhoDTO, response.getBody());
    }

    @Test
    public void testUpdateMetaSonho() {
        when(metaSonhoService.getMetaSonhoById(any(UUID.class)))
                .thenReturn(metaSonho);
        when(metaSonhoService.updateMetaSonho(any(UUID.class), any(MetaSonho.class)))
                .thenReturn(metaSonho);

        ResponseEntity<MetaSonhoDTO> response = controller.updateMetaSonho(metaSonho.getId(), metaSonhoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(metaSonhoDTO, response.getBody());
    }

    @Test
    public void testUpdateMetaSonhoNotFound() {
        when(metaSonhoService.getMetaSonhoById(any(UUID.class)))
                .thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<MetaSonhoDTO> response = controller.updateMetaSonho(UUID.randomUUID(), metaSonhoDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteMetaSonho() {
        doNothing().when(metaSonhoService).deleteMetaSonho(any(UUID.class));

        ResponseEntity<Void> response = controller.deleteMetaSonho(metaSonho.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteMetaSonhoNotFound() {
        doThrow(new RuntimeException("Not Found")).when(metaSonhoService).deleteMetaSonho(any(UUID.class));

        ResponseEntity<Void> response = controller.deleteMetaSonho(UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testVerificarEconomiaEGuardar() {
        when(metaSonhoService.verificarEconomiaEGuardar(any(UUID.class)))
                .thenReturn("Economia verificada e salva");

        String response = controller.verificarEconomiaEGuardar(usuarioId);

        assertEquals("Economia verificada e salva", response);
    }
}
