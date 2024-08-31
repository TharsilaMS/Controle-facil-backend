package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.ResourceNotFoundException;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.PrevisaoGastosRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrevisaoGastosService {

    @Autowired
    private PrevisaoGastosRepository previsaoGastosRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public PrevisaoGastos createPrevisaoGastos(PrevisaoGastos previsaoGastos) {
        Usuario usuario = usuarioRepository.findById(previsaoGastos.getUsuario().getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        previsaoGastos.setUsuario(usuario);
        previsaoGastos.setDataRevisao(LocalDate.now().withDayOfMonth(1)); // Inicializa com o primeiro dia do mês

        return previsaoGastosRepository.save(previsaoGastos);
    }

    public PrevisaoGastos updatePrevisaoGastos(Long usuarioId, PrevisaoGastos previsaoGastos) {
        PrevisaoGastos existing = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new ResourceNotFoundException("Previsão de gastos não encontrada para o usuário"));

        existing.setLimiteGastos(previsaoGastos.getLimiteGastos());
        existing.setDataRevisao(LocalDate.now().withDayOfMonth(1)); // Atualiza para o início do mês atual

        return previsaoGastosRepository.save(existing);
    }

    public void updateGastosAtuais(Long usuarioId) {
        PrevisaoGastos previsaoGastos = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new ResourceNotFoundException("Previsão de gastos não encontrada para o usuário"));

        BigDecimal totalDespesas = despesaRepository.findByUsuario_IdUsuario(usuarioId).stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        previsaoGastos.setGastosAtuais(totalDespesas);
        previsaoGastosRepository.save(previsaoGastos);
    }

    public void verificarLimite(Long usuarioId) {
        PrevisaoGastos previsaoGastos = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new ResourceNotFoundException("Previsão de gastos não encontrada para o usuário"));

        BigDecimal totalDespesas = previsaoGastos.getGastosAtuais();

        if (totalDespesas.compareTo(previsaoGastos.getLimiteGastos()) > 0) {
            // Notificar usuário que o limite foi ultrapassado
            System.out.println("O limite de gastos foi ultrapassado!");
        }
    }

    public PrevisaoGastos getPrevisaoGastos(Long usuarioId) {
        return previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new ResourceNotFoundException("Previsão de gastos não encontrada para o usuário"));
    }
}
