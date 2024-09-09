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

@Component
public class Agendamento {

    @Autowired
    private PrevisaoGastosService previsaoGastosService;

    @Autowired
    private UsuarioService usuarioService;

    @Scheduled(cron = "0 0 0 1 * ?") // Executa no primeiro dia de cada mês à meia-noite
    public void resetPrevisaoGastos() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        LocalDate primeiraDataDoMes = LocalDate.now().withDayOfMonth(1);

        for (Usuario usuario : usuarios) {
            // Verifica se já existe uma previsão de gastos para o usuário e o mês atual
            PrevisaoGastos previsaoGastos = previsaoGastosService.findByUsuarioIdAndDataRevisao(usuario.getIdUsuario(), primeiraDataDoMes);

            if (previsaoGastos == null) {
                // Se não existir, cria uma nova previsão de gastos
                previsaoGastos = new PrevisaoGastos();
                previsaoGastos.setUsuario(usuario);
                previsaoGastos.setLimiteGastos(BigDecimal.ZERO); // Defina um valor padrão ou recupere de outra forma
                previsaoGastos.setGastosAtuais(BigDecimal.ZERO);
                previsaoGastos.setDataRevisao(primeiraDataDoMes);

                previsaoGastosService.createPrevisaoGastos(previsaoGastos);
            } else {
                // Se existir, apenas reseta os gastos atuais
                previsaoGastos.setGastosAtuais(BigDecimal.ZERO);
                previsaoGastos.setDataRevisao(primeiraDataDoMes);

                previsaoGastosService.updatePrevisaoGastos(usuario.getIdUsuario(), previsaoGastos);
            }
        }
    }
}
