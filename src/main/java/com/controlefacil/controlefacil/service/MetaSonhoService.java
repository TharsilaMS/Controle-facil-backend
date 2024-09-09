package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.MetaSonho;
import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Status;
import com.controlefacil.controlefacil.repository.MetaSonhosRepository;
import com.controlefacil.controlefacil.repository.PrevisaoGastosRepository;
import com.controlefacil.controlefacil.util.ConversorDeData;
import com.controlefacil.controlefacil.exception.MetaAtivaExistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MetaSonhoService {

    @Autowired
    private MetaSonhosRepository metaSonhoRepository;

    @Autowired
    private PrevisaoGastosRepository previsaoGastosRepository;

    public MetaSonho createMetaSonho(MetaSonho metaSonho) {

        List<MetaSonho> metasAtivas = metaSonhoRepository.findByUsuario_IdUsuarioAndStatus(
                metaSonho.getUsuario().getIdUsuario(),
                Status.ATIVA
        );

        if (!metasAtivas.isEmpty()) {
            throw new MetaAtivaExistenteException("O usuário já possui uma meta ativa. Conclua a meta atual antes de criar uma nova.");
        }

        return metaSonhoRepository.save(metaSonho);
    }

    public MetaSonho updateMetaSonho(UUID id, MetaSonho metaSonho) {
        if (metaSonhoRepository.existsById(id)) {
            MetaSonho metaExistente = metaSonhoRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Meta de Sonho não encontrada com ID: " + id));
            if (metaExistente.getStatus() != Status.CONCLUIDA && !metaExistente.getId().equals(metaSonho.getId())) {
                throw new RuntimeException("Não é permitido atualizar a meta ativa. Conclua a meta atual antes de atualizar.");
            }

            metaSonho.setId(id);
            return metaSonhoRepository.save(metaSonho);
        } else {
            throw new RuntimeException("Meta de Sonho não encontrada com ID: " + id);
        }
    }

    public MetaSonho getMetaSonhoById(UUID id) {
        return metaSonhoRepository.findById(id).orElseThrow(() -> new RuntimeException("Meta de Sonho não encontrada com ID: " + id));
    }

    public List<MetaSonho> getAllMetaSonhosByUsuarioId(UUID usuarioId) {
        return metaSonhoRepository.findByUsuario_IdUsuario(usuarioId);
    }

    public void deleteMetaSonho(UUID id) {
        if (metaSonhoRepository.existsById(id)) {
            MetaSonho metaSonho = metaSonhoRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Meta de Sonho não encontrada com ID: " + id));

            // Não permita a exclusão de uma meta ativa
            if (metaSonho.getStatus() == Status.ATIVA) {
                throw new RuntimeException("Não é permitido excluir uma meta ativa. Conclua a meta antes de excluí-la.");
            }

            metaSonhoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Meta de Sonho não encontrada com ID: " + id);
        }
    }

    public String verificarEconomiaEGuardar(UUID usuarioId) {
        Optional<PrevisaoGastos> previsaoGastosOpt = previsaoGastosRepository.findById(usuarioId);
        if (previsaoGastosOpt.isPresent()) {
            PrevisaoGastos previsaoGastos = previsaoGastosOpt.get();
            BigDecimal limiteGastos = previsaoGastos.getLimiteGastos();
            BigDecimal gastosAtuais = previsaoGastos.getGastosAtuais();
            BigDecimal economia = limiteGastos.subtract(gastosAtuais);

            if (economia.compareTo(BigDecimal.ZERO) > 0) {
                List<MetaSonho> metas = metaSonhoRepository.findByUsuario_IdUsuarioAndStatus(
                        usuarioId, Status.ATIVA
                );

                if (!metas.isEmpty()) {
                    MetaSonho metaSonho = metas.get(0);
                    metaSonho.setValorEconomizado(metaSonho.getValorEconomizado().add(economia));

                    verificarMetaAlcancada(metaSonho);

                    metaSonhoRepository.save(metaSonho);
                    return "Você economizou R$ " + economia + ". Esse valor foi adicionado à sua meta de sonho: " + metaSonho.getTitulo() + ".";
                } else {
                    return "Você economizou R$ " + economia + ", mas não há metas de sonho definidas.";
                }
            } else {
                return "Não há economia para guardar este mês.";
            }
        } else {
            return "Previsão de gastos não encontrada para o usuário.";
        }
    }

    public void verificarMetaAlcancada(MetaSonho metaSonho) {
        BigDecimal valorEconomizado = metaSonho.getValorEconomizado();
        BigDecimal valorAlvo = metaSonho.getValorAlvo();

        if (valorEconomizado.compareTo(valorAlvo) >= 0) {
            metaSonho.setStatus(Status.CONCLUIDA);
            metaSonhoRepository.save(metaSonho);
        }
    }

    public LocalDate convertToLocalDate(String dateString) {
        return ConversorDeData.parseDate(dateString);
    }

    public LocalDateTime convertToLocalDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIDNIGHT);
    }
}
