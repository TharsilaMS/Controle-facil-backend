package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.RendaDTO;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.RendaService;
import com.controlefacil.controlefacil.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador responsável por gerenciar as rendas dos usuários.
 * Este controlador permite criar, atualizar, obter e deletar informações de renda.
 */
@RestController
@RequestMapping("/api/rendas")
public class RendaController {

    @Autowired
    private RendaService rendaService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Recupera todas as rendas registradas.
     *
     * @return Uma lista de objetos RendaDTO representando todas as rendas.
     */
    @GetMapping
    public List<RendaDTO> getAllRendas() {
        return rendaService.getAllRendas().stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Recupera uma renda específica pelo seu ID.
     *
     * @param id ID da renda a ser recuperada
     * @return A renda correspondente, se encontrada, ou uma resposta "não encontrado"
     */
    @GetMapping("/{id}")
    public ResponseEntity<RendaDTO> getRendaById(@PathVariable UUID id) {
        Optional<Renda> renda = rendaService.getRendaById(id);
        return renda.map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova renda.
     *
     * @param rendaDTO Dados da renda a ser criada
     * @return A renda criada, com status de resposta "Criado", ou uma resposta "bad request" se o usuário não estiver presente
     */
    @PostMapping
    public ResponseEntity<RendaDTO> createRenda(@RequestBody RendaDTO rendaDTO) {
        if (rendaDTO.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Usuario usuario = new Usuario(rendaDTO.getUsuarioId());
        Renda renda = new Renda();
        renda.setUsuario(usuario);
        renda.setDescricao(rendaDTO.getDescricao());
        renda.setValor(rendaDTO.getValor());
        renda.setData(rendaDTO.getData());
        Renda novaRenda = rendaService.saveRenda(renda);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(novaRenda));
    }

    /**
     * Atualiza uma renda existente.
     *
     * @param id ID da renda a ser atualizada
     * @param rendaDTO Dados atualizados da renda
     * @return A renda atualizada, ou uma resposta "não encontrado" se a renda não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<RendaDTO> updateRenda(@PathVariable UUID id, @RequestBody RendaDTO rendaDTO) {
        Optional<Renda> existingRenda = rendaService.getRendaById(id);
        if (existingRenda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Renda renda = existingRenda.get();
        renda.setUsuario(new Usuario(rendaDTO.getUsuarioId()));
        renda.setDescricao(rendaDTO.getDescricao());
        renda.setValor(rendaDTO.getValor());
        renda.setData(rendaDTO.getData());
        Renda updatedRenda = rendaService.saveRenda(renda);
        return ResponseEntity.ok(convertToDTO(updatedRenda));
    }

    /**
     * Deleta uma renda específica pelo seu ID.
     *
     * @param id ID da renda a ser deletada
     * @return Uma resposta "no content" se a renda foi deletada, ou uma resposta "não encontrado" se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRenda(@PathVariable UUID id) {
        Optional<Renda> existingRenda = rendaService.getRendaById(id);
        if (existingRenda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        rendaService.deleteRenda(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Converte uma entidade Renda em um objeto RendaDTO.
     *
     * @param renda A entidade Renda a ser convertida
     * @return O objeto RendaDTO correspondente
     */
    private RendaDTO convertToDTO(Renda renda) {
        return new RendaDTO(
                renda.getId(),
                renda.getUsuario().getIdUsuario(),
                renda.getDescricao(),
                renda.getValor(),
                renda.getData()
        );
    }
}
