package com.controlefacil.controlefacil.component;

import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.PrevisaoGastosService;
import com.controlefacil.controlefacil.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class AgendamentoTest {

    @Mock
    private PrevisaoGastosService previsaoGastosService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private Agendamento agendamento;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testResetPrevisaoGastos_CriaNovaPrevisao() {
        // Arrange
        Usuario usuario = new Usuario();
        UUID usuarioId = UUID.randomUUID(); // Gerar um UUID aleatório
        usuario.setIdUsuario(usuarioId);

        List<Usuario> usuarios = Arrays.asList(usuario);
        LocalDate primeiraDataDoMes = LocalDate.now().withDayOfMonth(1);

        when(usuarioService.getAllUsuarios()).thenReturn(usuarios);
        when(previsaoGastosService.findByUsuarioIdAndDataRevisao(usuario.getIdUsuario(), primeiraDataDoMes)).thenReturn(null);

        // Act
        agendamento.resetPrevisaoGastos();

        // Assert
        verify(previsaoGastosService).createPrevisaoGastos(any(PrevisaoGastos.class));
    }

    @Test
    public void testResetPrevisaoGastos_AtualizaPrevisaoExistente() {
        // Arrange
        Usuario usuario = new Usuario();
        UUID usuarioId = UUID.randomUUID(); // Gerar um UUID aleatório
        usuario.setIdUsuario(usuarioId);

        List<Usuario> usuarios = Arrays.asList(usuario);
        LocalDate primeiraDataDoMes = LocalDate.now().withDayOfMonth(1);

        PrevisaoGastos previsaoGastosExistente = new PrevisaoGastos();
        previsaoGastosExistente.setGastosAtuais(BigDecimal.TEN);

        when(usuarioService.getAllUsuarios()).thenReturn(usuarios);
        when(previsaoGastosService.findByUsuarioIdAndDataRevisao(usuario.getIdUsuario(), primeiraDataDoMes)).thenReturn(previsaoGastosExistente);

        // Act
        agendamento.resetPrevisaoGastos();

        // Assert
        // Remover verificações diretas em `previsaoGastosExistente`
        verify(previsaoGastosService).updatePrevisaoGastos(eq(usuario.getIdUsuario()), any(PrevisaoGastos.class));
    }
}
