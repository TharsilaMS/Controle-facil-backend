package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.PrevisaoGastosExistenteException;
import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
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
import java.util.UUID;

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
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        previsaoGastos.setUsuario(usuario);
        previsaoGastos.setDataRevisao(LocalDate.now().withDayOfMonth(1));

        PrevisaoGastos existente = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(
                previsaoGastos.getUsuario().getIdUsuario(),
                previsaoGastos.getDataRevisao()
        ).orElse(null);

        if (existente != null) {
            throw new PrevisaoGastosExistenteException("Já existe uma previsão de gastos para o usuário no mês atual.");
        }

        return previsaoGastosRepository.save(previsaoGastos);
    }

    public PrevisaoGastos updatePrevisaoGastos(UUID usuarioId, PrevisaoGastos previsaoGastos) {
        PrevisaoGastos existing = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de gastos não encontrada para o usuário"));

        existing.setLimiteGastos(previsaoGastos.getLimiteGastos());
        existing.setDataRevisao(LocalDate.now().withDayOfMonth(1));

        return previsaoGastosRepository.save(existing);
    }

    public void updateGastosAtuais(UUID usuarioId) {
        PrevisaoGastos previsaoGastos = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de gastos não encontrada para o usuário"));

        BigDecimal totalDespesas = despesaRepository.findByUsuario_IdUsuario(usuarioId).stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        previsaoGastos.setGastosAtuais(totalDespesas);
        previsaoGastosRepository.save(previsaoGastos);
    }

    public void verificarLimite(UUID usuarioId) {
        PrevisaoGastos previsaoGastos = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de gastos não encontrada para o usuário"));

        BigDecimal totalDespesas = previsaoGastos.getGastosAtuais();

        if (totalDespesas.compareTo(previsaoGastos.getLimiteGastos()) > 0) {
            System.out.println("O limite de gastos foi ultrapassado!");
        }
    }

    public PrevisaoGastos getPrevisaoGastos(UUID usuarioId) {
        return previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de gastos não encontrada para o usuário"));
    }
    public PrevisaoGastos findByUsuarioIdAndDataRevisao(UUID usuarioId, LocalDate dataRevisao) {
        return previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, dataRevisao)
                .orElse(null);
    }

}
