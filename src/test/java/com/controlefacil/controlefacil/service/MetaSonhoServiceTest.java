package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.MetaAtivaExistenteException;
import com.controlefacil.controlefacil.model.MetaSonho;
import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Status;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.MetaSonhosRepository;
import com.controlefacil.controlefacil.repository.PrevisaoGastosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MetaSonhoServiceTest {

    @InjectMocks
    private MetaSonhoService metaSonhoService;

    @Mock
    private MetaSonhosRepository metaSonhoRepository;

    @Mock
    private PrevisaoGastosRepository previsaoGastosRepository;

    private MetaSonho metaSonho;
    private PrevisaoGastos previsaoGastos;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        metaSonho = new MetaSonho();
        metaSonho.setId(UUID.randomUUID());
        metaSonho.setTitulo("Meta de Viagem");
        metaSonho.setValorAlvo(new BigDecimal("500.00"));
        metaSonho.setValorEconomizado(BigDecimal.ZERO);
        metaSonho.setStatus(Status.ATIVA);

        previsaoGastos = new PrevisaoGastos();
        previsaoGastos.setLimiteGastos(new BigDecimal("600.00"));
        previsaoGastos.setGastosAtuais(new BigDecimal("400.00"));
    }

    @Test
    public void testCreateMetaSonho() {
        when(metaSonhoRepository.save(metaSonho)).thenReturn(metaSonho);

        MetaSonho result = metaSonhoService.createMetaSonho(metaSonho);

        assertEquals(metaSonho, result);
        verify(metaSonhoRepository).save(metaSonho);
    }

    @Test
    public void testUpdateMetaSonho() {
        UUID id = metaSonho.getId();
        MetaSonho updatedMetaSonho = new MetaSonho();
        updatedMetaSonho.setId(id);
        updatedMetaSonho.setTitulo("Meta Atualizada");
        updatedMetaSonho.setValorAlvo(new BigDecimal("600.00"));
        updatedMetaSonho.setValorEconomizado(new BigDecimal("100.00"));
        updatedMetaSonho.setStatus(Status.ATIVA);

        when(metaSonhoRepository.existsById(id)).thenReturn(true);
        when(metaSonhoRepository.save(updatedMetaSonho)).thenReturn(updatedMetaSonho);

        MetaSonho result = metaSonhoService.updateMetaSonho(id, updatedMetaSonho);

        assertEquals(updatedMetaSonho, result);
        verify(metaSonhoRepository).save(updatedMetaSonho);
    }

    @Test
    public void testUpdateMetaSonhoNotFound() {
        UUID id = UUID.randomUUID();
        when(metaSonhoRepository.existsById(id)).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            metaSonhoService.updateMetaSonho(id, metaSonho);
        });

        assertEquals("Meta de Sonho não encontrada com ID: " + id, thrown.getMessage());
    }

    @Test
    public void testGetMetaSonhoById() {
        UUID id = metaSonho.getId();
        when(metaSonhoRepository.findById(id)).thenReturn(Optional.of(metaSonho));

        MetaSonho result = metaSonhoService.getMetaSonhoById(id);

        assertEquals(metaSonho, result);
    }

    @Test
    public void testGetMetaSonhoByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(metaSonhoRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            metaSonhoService.getMetaSonhoById(id);
        });

        assertEquals("Meta de Sonho não encontrada com ID: " + id, thrown.getMessage());
    }

    @Test
    public void testGetAllMetaSonhosByUsuarioId() {
        UUID usuarioId = UUID.randomUUID();
        when(metaSonhoRepository.findByUsuario_IdUsuario(usuarioId)).thenReturn(List.of(metaSonho));

        List<MetaSonho> result = metaSonhoService.getAllMetaSonhosByUsuarioId(usuarioId);

        assertEquals(1, result.size());
        assertEquals(metaSonho, result.get(0));
    }

    @Test
    public void testDeleteMetaSonho() {
        UUID id = metaSonho.getId();
        when(metaSonhoRepository.existsById(id)).thenReturn(true);

        metaSonhoService.deleteMetaSonho(id);

        verify(metaSonhoRepository).deleteById(id);
    }

    @Test
    public void testDeleteMetaSonhoNotFound() {
        UUID id = UUID.randomUUID();
        when(metaSonhoRepository.existsById(id)).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            metaSonhoService.deleteMetaSonho(id);
        });

        assertEquals("Meta de Sonho não encontrada com ID: " + id, thrown.getMessage());
    }

    @Test
    public void testVerificarEconomiaEGuardar() {
        UUID usuarioId = UUID.randomUUID();
        metaSonho.setUsuario(new Usuario(usuarioId));  // Assuming Usuario is a class with UUID as ID

        when(previsaoGastosRepository.findById(usuarioId)).thenReturn(Optional.of(previsaoGastos));
        when(metaSonhoRepository.findByUsuario_IdUsuarioAndStatus(usuarioId, Status.ATIVA))
                .thenReturn(List.of(metaSonho));
        when(metaSonhoRepository.save(metaSonho)).thenReturn(metaSonho);

        String result = metaSonhoService.verificarEconomiaEGuardar(usuarioId);

        assertEquals("Você economizou R$ 200.00. Esse valor foi adicionado à sua meta de sonho: Meta de Viagem.", result);
        assertEquals(new BigDecimal("200.00"), metaSonho.getValorEconomizado());
        verify(metaSonhoRepository).save(metaSonho);
    }

    @Test
    public void testVerificarEconomiaEGuardarSemMeta() {
        UUID usuarioId = UUID.randomUUID();
        when(previsaoGastosRepository.findById(usuarioId)).thenReturn(Optional.of(previsaoGastos));
        when(metaSonhoRepository.findByUsuario_IdUsuarioAndStatus(usuarioId, Status.ATIVA))
                .thenReturn(Collections.emptyList());

        String result = metaSonhoService.verificarEconomiaEGuardar(usuarioId);

        assertEquals("Você economizou R$ 200.00, mas não há metas de sonho definidas.", result);
    }

    @Test
    public void testVerificarEconomiaEGuardarSemEconomia() {
        previsaoGastos.setGastosAtuais(new BigDecimal("600.00"));

        UUID usuarioId = UUID.randomUUID();
        when(previsaoGastosRepository.findById(usuarioId)).thenReturn(Optional.of(previsaoGastos));

        String result = metaSonhoService.verificarEconomiaEGuardar(usuarioId);

        assertEquals("Não há economia para guardar este mês.", result);
    }

    @Test
    public void testVerificarEconomiaEGuardarPrevisaoNotFound() {
        UUID usuarioId = UUID.randomUUID();
        when(previsaoGastosRepository.findById(usuarioId)).thenReturn(Optional.empty());

        String result = metaSonhoService.verificarEconomiaEGuardar(usuarioId);

        assertEquals("Previsão de gastos não encontrada para o usuário.", result);
    }

    @Test
    public void testVerificarMetaAlcancada() {
        metaSonho.setValorEconomizado(new BigDecimal("500.00"));
        metaSonho.setValorAlvo(new BigDecimal("500.00"));

        metaSonhoService.verificarMetaAlcancada(metaSonho);

        assertEquals(Status.CONCLUIDA, metaSonho.getStatus());
        verify(metaSonhoRepository).save(metaSonho);
    }

    @Test
    public void testConvertToLocalDate() {
        String dateString = "06/09/2024";
        LocalDate expectedDate = LocalDate.of(2024, 9, 6);
        LocalDate result = metaSonhoService.convertToLocalDate(dateString);

        assertEquals(expectedDate, result);
    }

    @Test
    public void testConvertToLocalDateTime() {
        LocalDate date = LocalDate.of(2024, 9, 6);
        LocalDateTime expectedDateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        LocalDateTime result = metaSonhoService.convertToLocalDateTime(date);

        assertEquals(expectedDateTime, result);
    }
}
