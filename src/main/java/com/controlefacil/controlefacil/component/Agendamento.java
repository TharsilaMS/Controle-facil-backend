package com.controlefacil.controlefacil.component;

import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.PrevisaoGastosService;
import com.controlefacil.controlefacil.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe responsável por agendar tarefas relacionadas à previsão de gastos.
 * Esta classe utiliza agendamento automático para redefinir as previsões de gastos dos usuários no início de cada mês.
 */
@Component
public class Agendamento {

    @Autowired
    private PrevisaoGastosService previsaoGastosService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Método agendado que é executado no primeiro dia de cada mês à meia-noite.
     * Este método redefine a previsão de gastos para todos os usuários,
     * estabelecendo o limite e os gastos atuais como zero.
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetPrevisaoGastos() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        LocalDate primeiraDataDoMes = LocalDate.now().withDayOfMonth(1);

        for (Usuario usuario : usuarios) {
            PrevisaoGastos previsaoGastos = previsaoGastosService.findByUsuarioIdAndDataRevisao(usuario.getIdUsuario(), primeiraDataDoMes);

            if (previsaoGastos == null) {
                previsaoGastos = new PrevisaoGastos();
                previsaoGastos.setUsuario(usuario);
                previsaoGastos.setLimiteGastos(BigDecimal.ZERO);
                previsaoGastos.setGastosAtuais(BigDecimal.ZERO);
                previsaoGastos.setDataRevisao(primeiraDataDoMes);

                previsaoGastosService.createPrevisaoGastos(previsaoGastos);
            } else {
                previsaoGastos.setGastosAtuais(BigDecimal.ZERO);
                previsaoGastos.setDataRevisao(primeiraDataDoMes);

                previsaoGastosService.updatePrevisaoGastos(usuario.getIdUsuario(), previsaoGastos);
            }
        }
    }
}
