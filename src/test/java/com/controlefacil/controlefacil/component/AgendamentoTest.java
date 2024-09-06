package com.controlefacil.controlefacil.component;

import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.PrevisaoGastosService;
import com.controlefacil.controlefacil.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AgendamentoTest {

    @Mock
    private PrevisaoGastosService previsaoGastosService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private Agendamento agendamento;

    public AgendamentoTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testResetPrevisaoGastos() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        PrevisaoGastos previsaoGastos = new PrevisaoGastos();
        previsaoGastos.setId(1L);
        previsaoGastos.setGastosAtuais(BigDecimal.TEN);
        previsaoGastos.setDataRevisao(LocalDate.of(2024, 9, 1));

        when(usuarioService.getAllUsuarios()).thenReturn(Collections.singletonList(usuario));
        when(previsaoGastosService.getPrevisaoGastos(usuario.getIdUsuario())).thenReturn(previsaoGastos);

        agendamento.resetPrevisaoGastos();

        verify(previsaoGastosService).getPrevisaoGastos(usuario.getIdUsuario());
        verify(previsaoGastosService).createPrevisaoGastos(argThat(p ->
                p.getGastosAtuais().equals(BigDecimal.ZERO) &&
                        p.getDataRevisao().isEqual(LocalDate.now().withDayOfMonth(1))
        ));
    }
}
