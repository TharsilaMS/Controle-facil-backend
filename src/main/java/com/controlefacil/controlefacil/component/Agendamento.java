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

    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetPrevisaoGastos() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        for (Usuario usuario : usuarios) {
            PrevisaoGastos previsaoGastos = previsaoGastosService.getPrevisaoGastos(usuario.getIdUsuario());

            previsaoGastos.setGastosAtuais(BigDecimal.ZERO);
            previsaoGastos.setDataRevisao(LocalDate.now().withDayOfMonth(1));

            previsaoGastosService.createPrevisaoGastos(previsaoGastos);
        }
    }
}
