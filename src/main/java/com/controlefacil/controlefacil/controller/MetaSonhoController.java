package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.MetaSonhoDTO;
import com.controlefacil.controlefacil.model.MetaSonho;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.MetaSonhoService;
import com.controlefacil.controlefacil.util.ConversorDeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador responsável pelas operações relacionadas às metas de sonho.
 * Proporciona endpoints para CRUD (Criar, Ler, Atualizar e Deletar) metas de sonho.
 */
@RestController
@RequestMapping("/api/metas-sonho")
public class MetaSonhoController {

    @Autowired
    private MetaSonhoService metaSonhoService;

    /**
     * Obtém todas as metas de sonho de um usuário específico.
     *
     * @param usuarioId ID do usuário cujas metas de sonho serão recuperadas
     * @return Lista de metas de sonho do usuário
     */
    @GetMapping
    public ResponseEntity<List<MetaSonhoDTO>> getAllMetasSonho(@RequestParam UUID usuarioId) {
        List<MetaSonhoDTO> metaSonhoDTOs = metaSonhoService.getAllMetaSonhosByUsuarioId(usuarioId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(metaSonhoDTOs);
    }

    /**
     * Recupera uma meta de sonho específica pelo ID.
     *
     * @param id ID da meta de sonho a ser recuperada
     * @return A meta de sonho correspondente ou uma resposta "não encontrado" se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaSonhoDTO> getMetaSonhoById(@PathVariable UUID id) {
        try {
            MetaSonho metaSonho = metaSonhoService.getMetaSonhoById(id);
            return ResponseEntity.ok(convertToDTO(metaSonho));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cria uma nova meta de sonho.
     *
     * @param metaSonhoDTO Dados da nova meta de sonho a ser criada
     * @return A meta de sonho criada com status de resposta "Criado"
     */
    @PostMapping
    public ResponseEntity<MetaSonhoDTO> createMetaSonho(@RequestBody MetaSonhoDTO metaSonhoDTO) {
        LocalDate prazoDate = ConversorDeData.parseDate(metaSonhoDTO.getPrazo());
        LocalDateTime prazoDateTime = LocalDateTime.of(prazoDate, LocalTime.MIDNIGHT);

        MetaSonho metaSonho = new MetaSonho();
        metaSonho.setTitulo(metaSonhoDTO.getTitulo());
        metaSonho.setDescricao(metaSonhoDTO.getDescricao());
        metaSonho.setValorAlvo(metaSonhoDTO.getValorAlvo());
        metaSonho.setValorTotal(metaSonhoDTO.getValorAlvo());
        metaSonho.setValorEconomizado(BigDecimal.ZERO);
        metaSonho.setDataCriacao(LocalDateTime.now());
        metaSonho.setPrazo(prazoDateTime);
        metaSonho.setUsuario(new Usuario(metaSonhoDTO.getUsuarioId()));

        MetaSonho createdMetaSonho = metaSonhoService.createMetaSonho(metaSonho);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdMetaSonho));
    }

    /**
     * Atualiza uma meta de sonho existente.
     *
     * @param id         ID da meta de sonho a ser atualizada
     * @param metaSonhoDTO Dados atualizados da meta de sonho
     * @return A meta de sonho atualizada ou uma resposta "não encontrado" se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaSonhoDTO> updateMetaSonho(@PathVariable UUID id, @RequestBody MetaSonhoDTO metaSonhoDTO) {
        try {
            MetaSonho metaSonho = metaSonhoService.getMetaSonhoById(id);
            LocalDate prazoDate = ConversorDeData.parseDate(metaSonhoDTO.getPrazo());
            LocalDateTime prazoDateTime = LocalDateTime.of(prazoDate, LocalTime.MIDNIGHT);

            metaSonho.setTitulo(metaSonhoDTO.getTitulo());
            metaSonho.setDescricao(metaSonhoDTO.getDescricao());
            metaSonho.setValorAlvo(metaSonhoDTO.getValorAlvo());
            metaSonho.setValorTotal(metaSonhoDTO.getValorAlvo());
            metaSonho.setPrazo(prazoDateTime);
            metaSonho.setUsuario(new Usuario(metaSonhoDTO.getUsuarioId()));

            MetaSonho updatedMetaSonho = metaSonhoService.updateMetaSonho(id, metaSonho);
            return ResponseEntity.ok(convertToDTO(updatedMetaSonho));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deleta uma meta de sonho específica.
     *
     * @param id ID da meta de sonho a ser deletada
     * @return Uma resposta sem conteúdo ou uma resposta "não encontrado" se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaSonho(@PathVariable UUID id) {
        try {
            metaSonhoService.deleteMetaSonho(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Verifica a economia do usuário e a guarda.
     *
     * @param usuarioId ID do usuário cuja economia será verificada
     * @return Mensagem sobre a verificação da economia
     */
    @PostMapping("/verificar-economia/{usuarioId}")
    public String verificarEconomiaEGuardar(@PathVariable UUID usuarioId) {
        return metaSonhoService.verificarEconomiaEGuardar(usuarioId);
    }

    /**
     * Adiciona um valor a uma meta de sonho existente.
     *
     * @param id ID da meta de sonho a ser atualizada
     * @param valorAdicional Valor a ser adicionado à meta
     * @return A meta de sonho atualizada
     */
    @PatchMapping("/{id}/adicionar-valor")
    public ResponseEntity<MetaSonhoDTO> adicionarValorMeta(
            @PathVariable UUID id,
            @RequestParam BigDecimal valorAdicional) {
        MetaSonho metaAtualizada = metaSonhoService.adicionarValorMeta(id, valorAdicional);
        MetaSonhoDTO metaSonhoDTO = convertToDTO(metaAtualizada);
        return ResponseEntity.ok(metaSonhoDTO);
    }

    /**
     * Converte uma entidade MetaSonho em um DTO.
     *
     * @param metaSonho Entidade a ser convertida
     * @return DTO correspondente da meta de sonho
     */
    private MetaSonhoDTO convertToDTO(MetaSonho metaSonho) {
        MetaSonhoDTO metaSonhoDTO = new MetaSonhoDTO();
        metaSonhoDTO.setId(metaSonho.getId());
        metaSonhoDTO.setTitulo(metaSonho.getTitulo());
        metaSonhoDTO.setDescricao(metaSonho.getDescricao());
        metaSonhoDTO.setValorAlvo(metaSonho.getValorAlvo());
        metaSonhoDTO.setValorEconomizado(metaSonho.getValorEconomizado());
        metaSonhoDTO.setPrazo(ConversorDeData.formatDate(metaSonho.getPrazo().toLocalDate()));
        metaSonhoDTO.setStatus(metaSonho.getStatus());
        metaSonhoDTO.setUsuarioId(metaSonho.getUsuario().getIdUsuario());
        return metaSonhoDTO;
    }
}
