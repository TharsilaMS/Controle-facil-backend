package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.RendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
    private UUID usuarioId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Gera um UUID para o usuário
        usuarioId = UUID.randomUUID();

        despesa1 = new Despesa();
        despesa1.setValor(new BigDecimal("50.00"));
        despesa1.setUsuario(new Usuario(usuarioId));

        despesa2 = new Despesa();
        despesa2.setValor(new BigDecimal("30.00"));
        despesa2.setUsuario(new Usuario(usuarioId));

        renda1 = new Renda();
        renda1.setValor(new BigDecimal("100.00"));
        renda1.setUsuario(new Usuario(usuarioId));

        renda2 = new Renda();
        renda2.setValor(new BigDecimal("200.00"));
        renda2.setUsuario(new Usuario(usuarioId));
    }

    @Test
    public void testCalcularSaldo() {
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
        when(despesaRepository.findByUsuario_IdUsuario(usuarioId))
                .thenReturn(List.of(despesa1, despesa2));

        List<Despesa> despesas = saldoService.getDespesasByUsuario(usuarioId);

        assertEquals(2, despesas.size());
        assertEquals(despesa1, despesas.get(0));
        assertEquals(despesa2, despesas.get(1));
    }

    @Test
    public void testGetRendasByUsuario() {
        when(rendaRepository.findByUsuario_IdUsuario(usuarioId))
                .thenReturn(List.of(renda1, renda2));

        List<Renda> rendas = saldoService.getRendasByUsuario(usuarioId);

        assertEquals(2, rendas.size());
        assertEquals(renda1, rendas.get(0));
        assertEquals(renda2, rendas.get(1));
    }
}
