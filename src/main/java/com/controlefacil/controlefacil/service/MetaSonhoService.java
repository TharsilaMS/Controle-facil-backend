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

/**
 * Serviço que gerencia as metas de sonhos dos usuários, permitindo criar,
 * atualizar, recuperar e excluir metas, além de gerenciar a economia do usuário.
 */
@Service
public class MetaSonhoService {

    @Autowired
    private MetaSonhosRepository metaSonhoRepository;

    @Autowired
    private PrevisaoGastosRepository previsaoGastosRepository;

    /**
     * Cria uma nova meta de sonho.
     *
     * @param metaSonho A meta de sonho a ser criada.
     * @return A meta de sonho criada.
     * @throws MetaAtivaExistenteException Se o usuário já tiver uma meta ativa.
     */
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

    /**
     * Atualiza uma meta de sonho existente.
     *
     * @param id O ID da meta a ser atualizada.
     * @param metaSonho Os novos dados da meta.
     * @return A meta de sonho atualizada.
     * @throws RuntimeException Se a meta não for encontrada ou se tentar atualizar uma meta ativa.
     */
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

    /**
     * Recupera uma meta de sonho pelo ID.
     *
     * @param id O ID da meta a ser recuperada.
     * @return A meta de sonho correspondente ao ID fornecido.
     * @throws RuntimeException Se a meta não for encontrada.
     */
    public MetaSonho getMetaSonhoById(UUID id) {
        return metaSonhoRepository.findById(id).orElseThrow(() -> new RuntimeException("Meta de Sonho não encontrada com ID: " + id));
    }

    /**
     * Recupera todas as metas de sonho de um usuário específico.
     *
     * @param usuarioId O ID do usuário cujas metas devem ser recuperadas.
     * @return Uma lista de metas de sonho associadas ao usuário.
     */
    public List<MetaSonho> getAllMetaSonhosByUsuarioId(UUID usuarioId) {
        return metaSonhoRepository.findByUsuario_IdUsuario(usuarioId);
    }

    /**
     * Exclui uma meta de sonho pelo ID.
     *
     * @param id O ID da meta a ser excluída.
     * @throws RuntimeException Se a meta não for encontrada ou se for uma meta ativa.
     */
    public void deleteMetaSonho(UUID id) {
        if (metaSonhoRepository.existsById(id)) {
            MetaSonho metaSonho = metaSonhoRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Meta de Sonho não encontrada com ID: " + id));
            if (metaSonho.getStatus() == Status.ATIVA) {
                throw new RuntimeException("Não é permitido excluir uma meta ativa. Conclua a meta antes de excluí-la.");
            }
            metaSonhoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Meta de Sonho não encontrada com ID: " + id);
        }
    }

    /**
     * Verifica se o usuário economizou algum valor e o adiciona à sua meta de sonho.
     *
     * @param usuarioId O ID do usuário.
     * @return Uma mensagem sobre a economia realizada e seu impacto na meta de sonho.
     */
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

    /**
     * Verifica se a meta de sonho foi alcançada e atualiza seu status se necessário.
     *
     * @param metaSonho A meta de sonho a ser verificada.
     */
    public void verificarMetaAlcancada(MetaSonho metaSonho) {
        BigDecimal valorEconomizado = metaSonho.getValorEconomizado();
        BigDecimal valorAlvo = metaSonho.getValorAlvo();

        if (valorEconomizado.compareTo(valorAlvo) >= 0) {
            metaSonho.setStatus(Status.CONCLUIDA);
            metaSonhoRepository.save(metaSonho);
        }
    }

    /**
     * Adiciona um valor adicional à meta de sonho existente.
     *
     * @param idMeta O ID da meta a ser atualizada.
     * @param valorAdicional O valor a ser adicionado.
     * @return A meta de sonho atualizada.
     * @throws RuntimeException Se a meta não for encontrada.
     */
    public MetaSonho adicionarValorMeta(UUID idMeta, BigDecimal valorAdicional) {
        MetaSonho metaSonho = metaSonhoRepository.findById(idMeta)
                .orElseThrow(() -> new RuntimeException("Meta de Sonho não encontrada com ID: " + idMeta));
        metaSonho.setValorEconomizado(metaSonho.getValorEconomizado().add(valorAdicional));
        verificarMetaAlcancada(metaSonho);
        return metaSonhoRepository.save(metaSonho);
    }

    /**
     * Converte uma string de data para um objeto LocalDate.
     *
     * @param dateString A string que representa uma data.
     * @return O objeto LocalDate correspondente.
     */
    public LocalDate convertToLocalDate(String dateString) {
        return ConversorDeData.parseDate(dateString);
    }

    /**
     * Converte um objeto LocalDate para LocalDateTime, definindo a hora como meia-noite.
     *
     * @param date O objeto LocalDate a ser convertido.
     * @return O objeto LocalDateTime correspondente.
     */
    public LocalDateTime convertToLocalDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIDNIGHT);
    }
}
