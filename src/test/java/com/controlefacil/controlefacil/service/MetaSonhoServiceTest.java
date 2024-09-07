package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.MetaSonho;
import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Status;
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
        metaSonho.setId(1L);
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
        when(metaSonhoRepository.existsById(1L)).thenReturn(true);
        when(metaSonhoRepository.save(metaSonho)).thenReturn(metaSonho);

        MetaSonho result = metaSonhoService.updateMetaSonho(1L, metaSonho);

        assertEquals(metaSonho, result);
        verify(metaSonhoRepository).save(metaSonho);
    }

    @Test
    public void testUpdateMetaSonhoNotFound() {
        when(metaSonhoRepository.existsById(1L)).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            metaSonhoService.updateMetaSonho(1L, metaSonho);
        });

        assertEquals("Meta de Sonho não encontrada com ID: 1", thrown.getMessage());
    }

    @Test
    public void testGetMetaSonhoById() {
        when(metaSonhoRepository.findById(1L)).thenReturn(Optional.of(metaSonho));

        MetaSonho result = metaSonhoService.getMetaSonhoById(1L);

        assertEquals(metaSonho, result);
    }

    @Test
    public void testGetMetaSonhoByIdNotFound() {
        when(metaSonhoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            metaSonhoService.getMetaSonhoById(1L);
        });

        assertEquals("Meta de Sonho não encontrada com ID: 1", thrown.getMessage());
    }

    @Test
    public void testGetAllMetaSonhosByUsuarioId() {
        when(metaSonhoRepository.findByUsuario_IdUsuario(1L)).thenReturn(List.of(metaSonho));

        List<MetaSonho> result = metaSonhoService.getAllMetaSonhosByUsuarioId(1L);

        assertEquals(1, result.size());
        assertEquals(metaSonho, result.get(0));
    }

    @Test
    public void testDeleteMetaSonho() {
        when(metaSonhoRepository.existsById(1L)).thenReturn(true);

        metaSonhoService.deleteMetaSonho(1L);

        verify(metaSonhoRepository).deleteById(1L);
    }

    @Test
    public void testDeleteMetaSonhoNotFound() {
        when(metaSonhoRepository.existsById(1L)).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            metaSonhoService.deleteMetaSonho(1L);
        });

        assertEquals("Meta de Sonho não encontrada com ID: 1", thrown.getMessage());
    }

    @Test
    public void testVerificarEconomiaEGuardar() {
        when(previsaoGastosRepository.findById(1L)).thenReturn(Optional.of(previsaoGastos));
        when(metaSonhoRepository.findByUsuario_IdUsuario(1L)).thenReturn(List.of(metaSonho));
        when(metaSonhoRepository.save(metaSonho)).thenReturn(metaSonho);

        String result = metaSonhoService.verificarEconomiaEGuardar(1L);

        assertEquals("Você economizou R$ 200.00. Esse valor foi adicionado à sua meta de sonho: Meta de Viagem.", result);
        assertEquals(new BigDecimal("200.00"), metaSonho.getValorEconomizado());
        verify(metaSonhoRepository).save(metaSonho);
    }

    @Test
    public void testVerificarEconomiaEGuardarSemMeta() {
        when(previsaoGastosRepository.findById(1L)).thenReturn(Optional.of(previsaoGastos));
        when(metaSonhoRepository.findByUsuario_IdUsuario(1L)).thenReturn(Collections.emptyList());

        String result = metaSonhoService.verificarEconomiaEGuardar(1L);

        assertEquals("Você economizou R$ 200.00, mas não há metas de sonho definidas.", result);
    }

    @Test
    public void testVerificarEconomiaEGuardarSemEconomia() {
        previsaoGastos.setGastosAtuais(new BigDecimal("600.00"));

        when(previsaoGastosRepository.findById(1L)).thenReturn(Optional.of(previsaoGastos));

        String result = metaSonhoService.verificarEconomiaEGuardar(1L);

        assertEquals("Não há economia para guardar este mês.", result);
    }

    @Test
    public void testVerificarEconomiaEGuardarPrevisaoNotFound() {
        when(previsaoGastosRepository.findById(1L)).thenReturn(Optional.empty());

        String result = metaSonhoService.verificarEconomiaEGuardar(1L);

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
