package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.RendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SaldoServiceTest {

    @InjectMocks
    private SaldoService saldoService;

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private RendaRepository rendaRepository;

    private Despesa despesa1;
    private Despesa despesa2;
    private Renda renda1;
    private Renda renda2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializa as despesas e rendas com valores de exemplo
        despesa1 = new Despesa();
        despesa1.setValor(new BigDecimal("50.00"));

        despesa2 = new Despesa();
        despesa2.setValor(new BigDecimal("30.00"));

        renda1 = new Renda();
        renda1.setValor(new BigDecimal("100.00"));

        renda2 = new Renda();
        renda2.setValor(new BigDecimal("200.00"));
    }

    @Test
    public void testCalcularSaldo() {
        Long usuarioId = 1L;

        when(despesaRepository.findByUsuario_IdUsuario(usuarioId))
                .thenReturn(List.of(despesa1, despesa2));
        when(rendaRepository.findByUsuario_IdUsuario(usuarioId))
                .thenReturn(List.of(renda1, renda2));

        BigDecimal saldoCalculado = saldoService.calcularSaldo(usuarioId);

        // Ajusta o valor esperado para refletir o cálculo correto
        BigDecimal expectedSaldo = new BigDecimal("220.00");
        assertEquals(expectedSaldo, saldoCalculado);
    }

    @Test
    public void testGetDespesasByUsuario() {
        Long usuarioId = 1L;

        when(despesaRepository.findByUsuario_IdUsuario(usuarioId))
                .thenReturn(List.of(despesa1, despesa2));

        List<Despesa> despesas = saldoService.getDespesasByUsuario(usuarioId);

        assertEquals(2, despesas.size());
        assertEquals(despesa1, despesas.get(0));
        assertEquals(despesa2, despesas.get(1));
    }

    @Test
    public void testGetRendasByUsuario() {
        Long usuarioId = 1L;

        when(rendaRepository.findByUsuario_IdUsuario(usuarioId))
                .thenReturn(List.of(renda1, renda2));

        List<Renda> rendas = saldoService.getRendasByUsuario(usuarioId);

        assertEquals(2, rendas.size());
        assertEquals(renda1, rendas.get(0));
        assertEquals(renda2, rendas.get(1));
    }
}
