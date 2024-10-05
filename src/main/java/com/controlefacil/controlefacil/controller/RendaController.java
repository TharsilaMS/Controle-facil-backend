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
 * Controlador responsável por gerenciar as requisições HTTP relacionadas ao recurso Renda.
 * Ele fornece endpoints para criar, recuperar, atualizar e deletar entradas de renda.
 */
@RestController
@RequestMapping("/api/rendas")
public class RendaController {

    @Autowired
    private RendaService rendaService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Recupera uma lista de todas as rendas do sistema.
     *
     * @return uma lista de objetos RendaDTO que representam todas as rendas
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
     * @param id o UUID da renda
     * @return um ResponseEntity contendo o RendaDTO se encontrado, ou 404 se não encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<RendaDTO> getRendaById(@PathVariable UUID id) {
        Optional<Renda> renda = rendaService.getRendaById(id);
        return renda.map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Recupera todas as rendas de um usuário específico.
     *
     * @param usuarioId o UUID do usuário
     * @return um ResponseEntity contendo uma lista de RendaDTOs associadas ao usuário
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<RendaDTO>> getRendasByUsuarioId(@PathVariable UUID usuarioId) {
        List<Renda> rendas = rendaService.getRendasByUsuarioId(usuarioId);
        List<RendaDTO> rendaDTOs = rendas.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(rendaDTOs);
    }

    /**
     * Cria uma nova renda para um usuário.
     *
     * @param rendaDTO os dados da nova renda no formato de um objeto RendaDTO
     * @return um ResponseEntity contendo o RendaDTO criado com status 201 (Created),
     * ou 400 (Bad Request) se o ID do usuário não for fornecido
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
        renda.setTipo(rendaDTO.getTipo());
        Renda novaRenda = rendaService.saveRenda(renda);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(novaRenda));
    }

    /**
     * Atualiza uma renda existente pelo seu ID.
     *
     * @param id o UUID da renda a ser atualizada
     * @param rendaDTO os novos dados da renda no formato de um objeto RendaDTO
     * @return um ResponseEntity contendo o RendaDTO atualizado ou 404 (Not Found) se a renda não for encontrada
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
        renda.setTipo(rendaDTO.getTipo()); // Atualizando o tipo
        Renda updatedRenda = rendaService.saveRenda(renda);
        return ResponseEntity.ok(convertToDTO(updatedRenda));
    }

    /**
     * Deleta uma renda específica pelo seu ID.
     *
     * @param id o UUID da renda a ser deletada
     * @return um ResponseEntity com status 204 (No Content) se a operação for bem-sucedida,
     * ou 404 (Not Found) se a renda não for encontrada
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
     * @param renda a entidade Renda a ser convertida
     * @return o objeto RendaDTO correspondente
     */
    private RendaDTO convertToDTO(Renda renda) {
        return new RendaDTO(
                renda.getId(),
                renda.getUsuario().getIdUsuario(),
                renda.getDescricao(),
                renda.getValor(),
                renda.getData(),
                renda.getTipo()
        );
    }
}
