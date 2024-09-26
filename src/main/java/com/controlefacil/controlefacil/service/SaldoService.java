package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.RendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pelo cálculo de saldo e gerenciamento de despesas e rendas dos usuários.
 * Este serviço permite calcular o saldo com base nas rendas e despesas de um usuário específico,
 * além de obter listas de despesas e rendas associadas a ele.
 */
@Service
public class SaldoService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private RendaRepository rendaRepository;

    /**
     * Calcula o saldo de um usuário com base em suas rendas e despesas.
     *
     * @param usuarioId O ID do usuário para o qual o saldo será calculado.
     * @return O saldo resultante, calculado como a diferença entre rendas e despesas.
     */
    public BigDecimal calcularSaldo(UUID usuarioId) {
        List<Despesa> despesas = despesaRepository.findByUsuario_IdUsuario(usuarioId);
        List<Renda> rendas = rendaRepository.findByUsuario_IdUsuario(usuarioId);

        BigDecimal totalDespesas = despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalRendas = rendas.stream()
                .map(Renda::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalRendas.subtract(totalDespesas);
    }

    /**
     * Obtém a lista de despesas associadas a um usuário específico.
     *
     * @param usuarioId O ID do usuário cujas despesas serão retornadas.
     * @return Uma lista de despesas do usuário.
     */
    public List<Despesa> getDespesasByUsuario(UUID usuarioId) {
        return despesaRepository.findByUsuario_IdUsuario(usuarioId);
    }

    /**
     * Obtém a lista de rendas associadas a um usuário específico.
     *
     * @param usuarioId O ID do usuário cujas rendas serão retornadas.
     * @return Uma lista de rendas do usuário.
     */
    public List<Renda> getRendasByUsuario(UUID usuarioId) {
        return rendaRepository.findByUsuario_IdUsuario(usuarioId);
    }
}
