package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.PrevisaoGastosRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PrevisaoGastosServiceTest {

    @InjectMocks
    private PrevisaoGastosService previsaoGastosService;

    @Mock
    private PrevisaoGastosRepository previsaoGastosRepository;

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private PrevisaoGastos previsaoGastos;
    private Usuario usuario;
    private Despesa despesa1;
    private Despesa despesa2;

    private UUID usuarioId;
    private UUID previsaoGastosId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioId = UUID.randomUUID();
        previsaoGastosId = UUID.randomUUID();

        usuario = new Usuario();
        usuario.setIdUsuario(usuarioId);

        previsaoGastos = new PrevisaoGastos();
        previsaoGastos.setId(previsaoGastosId);
        previsaoGastos.setUsuario(usuario);
        previsaoGastos.setLimiteGastos(new BigDecimal("200.00"));
        previsaoGastos.setGastosAtuais(BigDecimal.ZERO);
        previsaoGastos.setDataRevisao(LocalDate.now().withDayOfMonth(1));

        despesa1 = new Despesa();
        despesa1.setValor(new BigDecimal("50.00"));
        despesa1.setUsuario(usuario);

        despesa2 = new Despesa();
        despesa2.setValor(new BigDecimal("30.00"));
        despesa2.setUsuario(usuario);
    }

    @Test
    public void testCreatePrevisaoGastos() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(previsaoGastosRepository.save(previsaoGastos)).thenReturn(previsaoGastos);

        PrevisaoGastos result = previsaoGastosService.createPrevisaoGastos(previsaoGastos);

        assertEquals(previsaoGastos, result);
        verify(previsaoGastosRepository).save(previsaoGastos);
    }

    @Test
    public void testUpdatePrevisaoGastos() {
        PrevisaoGastos updatedPrevisao = new PrevisaoGastos();
        updatedPrevisao.setId(previsaoGastosId);
        updatedPrevisao.setLimiteGastos(new BigDecimal("300.00"));
        updatedPrevisao.setDataRevisao(LocalDate.now().withDayOfMonth(1));

        when(previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1)))
                .thenReturn(Optional.of(previsaoGastos));
        when(previsaoGastosRepository.save(previsaoGastos)).thenReturn(previsaoGastos);

        PrevisaoGastos result = previsaoGastosService.updatePrevisaoGastos(usuarioId, updatedPrevisao);

        assertEquals(new BigDecimal("300.00"), result.getLimiteGastos());
        verify(previsaoGastosRepository).save(previsaoGastos);
    }

    @Test
    public void testUpdateGastosAtuais() {
        previsaoGastos.setGastosAtuais(BigDecimal.ZERO);

        when(previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1)))
                .thenReturn(Optional.of(previsaoGastos));
        when(despesaRepository.findByUsuario_IdUsuario(usuarioId))
                .thenReturn(List.of(despesa1, despesa2));

        previsaoGastosService.updateGastosAtuais(usuarioId);

        verify(previsaoGastosRepository).save(previsaoGastos);
        assertEquals(new BigDecimal("80.00"), previsaoGastos.getGastosAtuais());
    }

    @Test
    public void testVerificarLimite() {
        previsaoGastos.setGastosAtuais(new BigDecimal("220.00"));

        when(previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1)))
                .thenReturn(Optional.of(previsaoGastos));

        previsaoGastosService.verificarLimite(usuarioId);

        // Aqui, verificamos se o método printou a mensagem no console
        // Para isso, podemos usar um sistema de capturas, ou apenas validar que o método foi chamado
        // No exemplo abaixo, o comportamento é apenas registrado
    }

    @Test
    public void testGetPrevisaoGastos() {
        when(previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1)))
                .thenReturn(Optional.of(previsaoGastos));

        PrevisaoGastos result = previsaoGastosService.getPrevisaoGastos(usuarioId);

        assertEquals(previsaoGastos, result);
    }

    @Test
    public void testCreatePrevisaoGastosUsuarioNotFound() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            previsaoGastosService.createPrevisaoGastos(previsaoGastos);
        });
    }

    @Test
    public void testUpdatePrevisaoGastosNotFound() {
        when(previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1)))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            previsaoGastosService.updatePrevisaoGastos(usuarioId, previsaoGastos);
        });
    }

    @Test
    public void testUpdateGastosAtuaisNotFound() {
        when(previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1)))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            previsaoGastosService.updateGastosAtuais(usuarioId);
        });
    }

    @Test
    public void testGetPrevisaoGastosNotFound() {
        when(previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1)))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            previsaoGastosService.getPrevisaoGastos(usuarioId);
        });
    }
}
