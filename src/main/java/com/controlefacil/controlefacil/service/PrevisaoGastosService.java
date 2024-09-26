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

/**
 * Serviço responsável pela gestão das previsões de gastos dos usuários.
 * Este serviço permite a criação, atualização e consulta das previsões de gastos,
 * além de verificar se o limite de gastos foi ultrapassado.
 */
@Service
public class PrevisaoGastosService {

    @Autowired
    private PrevisaoGastosRepository previsaoGastosRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Cria uma nova previsão de gastos para um usuário.
     *
     * @param previsaoGastos Objeto contendo os detalhes da previsão de gastos.
     * @return A previsão de gastos criada.
     * @throws RecursoNaoEncontradoException Se o usuário associado não for encontrado.
     * @throws PrevisaoGastosExistenteException Se já existir uma previsão de gastos para o mês atual.
     */
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

    /**
     * Atualiza a previsão de gastos de um usuário.
     *
     * @param usuarioId ID do usuário cuja previsão de gastos será atualizada.
     * @param previsaoGastos Objeto contendo os novos dados da previsão de gastos.
     * @return A previsão de gastos atualizada.
     * @throws RecursoNaoEncontradoException Se a previsão de gastos não for encontrada.
     */
    public PrevisaoGastos updatePrevisaoGastos(UUID usuarioId, PrevisaoGastos previsaoGastos) {
        PrevisaoGastos existing = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de gastos não encontrada para o usuário"));

        existing.setLimiteGastos(previsaoGastos.getLimiteGastos());
        existing.setDataRevisao(LocalDate.now().withDayOfMonth(1));

        return previsaoGastosRepository.save(existing);
    }

    /**
     * Atualiza os gastos atuais de um usuário com base em suas despesas.
     *
     * @param usuarioId ID do usuário cujos gastos atuais serão atualizados.
     * @throws RecursoNaoEncontradoException Se a previsão de gastos não for encontrada.
     */
    public void updateGastosAtuais(UUID usuarioId) {
        PrevisaoGastos previsaoGastos = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de gastos não encontrada para o usuário"));

        BigDecimal totalDespesas = despesaRepository.findByUsuario_IdUsuario(usuarioId).stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        previsaoGastos.setGastosAtuais(totalDespesas);
        previsaoGastosRepository.save(previsaoGastos);
    }

    /**
     * Verifica se o limite de gastos de um usuário foi ultrapassado.
     *
     * @param usuarioId ID do usuário cujo limite de gastos será verificado.
     * @throws RecursoNaoEncontradoException Se a previsão de gastos não for encontrada.
     */
    public void verificarLimite(UUID usuarioId) {
        PrevisaoGastos previsaoGastos = previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de gastos não encontrada para o usuário"));

        BigDecimal totalDespesas = previsaoGastos.getGastosAtuais();

        if (totalDespesas.compareTo(previsaoGastos.getLimiteGastos()) > 0) {
            System.out.println("O limite de gastos foi ultrapassado!");
        }
    }

    /**
     * Obtém a previsão de gastos de um usuário para o mês atual.
     *
     * @param usuarioId ID do usuário cuja previsão de gastos será obtida.
     * @return A previsão de gastos correspondente ao usuário.
     * @throws RecursoNaoEncontradoException Se a previsão de gastos não for encontrada.
     */
    public PrevisaoGastos getPrevisaoGastos(UUID usuarioId) {
        return previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, LocalDate.now().withDayOfMonth(1))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de gastos não encontrada para o usuário"));
    }

    /**
     * Busca uma previsão de gastos com base no ID do usuário e na data de revisão.
     *
     * @param usuarioId ID do usuário.
     * @param dataRevisao Data da revisão da previsão de gastos.
     * @return A previsão de gastos correspondente ou null se não for encontrada.
     */
    public PrevisaoGastos findByUsuarioIdAndDataRevisao(UUID usuarioId, LocalDate dataRevisao) {
        return previsaoGastosRepository.findByUsuario_IdUsuarioAndDataRevisao(usuarioId, dataRevisao)
                .orElse(null);
    }
}
