package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.SaldoDTO;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.model.TipoRenda;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.SaldoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SaldoControllerTest {

    @Mock
    private SaldoService saldoService;

    @InjectMocks
    private SaldoController saldoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSaldo() {
        UUID usuarioId = UUID.randomUUID();
        BigDecimal saldo = BigDecimal.valueOf(1000.00);
        when(saldoService.calcularSaldo(usuarioId)).thenReturn(saldo);

        ResponseEntity<SaldoDTO> response = saldoController.getSaldo(usuarioId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioId, Objects.requireNonNull(response.getBody()).getUsuarioId());
        assertEquals(saldo, response.getBody().getSaldo());
        verify(saldoService, times(1)).calcularSaldo(usuarioId);
    }

    @Test
    void testGetDespesasByUsuario() {
        UUID usuarioId = UUID.randomUUID();
        List<Despesa> despesas = Arrays.asList(
                new Despesa(UUID.randomUUID(), null, "Compra Supermercado", BigDecimal.valueOf(200.00), null, null, null),
                new Despesa(UUID.randomUUID(), null, "Aluguel", BigDecimal.valueOf(1200.00), null, null, null)
        );
        when(saldoService.getDespesasByUsuario(usuarioId)).thenReturn(despesas);

        ResponseEntity<List<Despesa>> response = saldoController.getDespesasByUsuario(usuarioId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(saldoService, times(1)).getDespesasByUsuario(usuarioId);
    }


    @Test
    void testGetRendasByUsuario() {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario(); // Supondo que você tenha uma classe Usuario, ajuste conforme necessário.
        usuario.setIdUsuario(usuarioId); // Defina o ID do usuário ou outros atributos necessários.

        List<Renda> rendas = Arrays.asList(
                new Renda(UUID.randomUUID(), usuario, "Salário", BigDecimal.valueOf(3000.00), LocalDate.now(), TipoRenda.SALARIO),
                new Renda(UUID.randomUUID(), usuario, "Freelance", BigDecimal.valueOf(1500.00), LocalDate.now(), TipoRenda.FREELANCE)
        );

        when(saldoService.getRendasByUsuario(usuarioId)).thenReturn(rendas);

        ResponseEntity<List<Renda>> response = saldoController.getRendasByUsuario(usuarioId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(saldoService, times(1)).getRendasByUsuario(usuarioId);
    }

}