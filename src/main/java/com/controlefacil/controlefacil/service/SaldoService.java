package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.RendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SaldoService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private RendaRepository rendaRepository;

    public BigDecimal calcularSaldo(Long usuarioId) {
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

    public List<Despesa> getDespesasByUsuario(Long usuarioId) {
        return despesaRepository.findByUsuario_IdUsuario(usuarioId);
    }

    public List<Renda> getRendasByUsuario(Long usuarioId) {
        return rendaRepository.findByUsuario_IdUsuario(usuarioId);
    }
}
