package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.DespesaDTO;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.CategoriaDespesaService;
import com.controlefacil.controlefacil.service.DespesaService;
import com.controlefacil.controlefacil.service.UsuarioService;
import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador REST para gerenciar despesas dos usuários.
 * Proporciona endpoints para CRUD (Criar, Ler, Atualizar e Deletar) despesas.
 */
@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaDespesaService categoriaDespesaService;

    /**
     * Retorna uma lista de todas as despesas cadastradas.
     *
     * @return ResponseEntity contendo uma lista de DespesaDTO com as despesas.
     */
    @GetMapping
    public ResponseEntity<List<DespesaDTO>> getAllDespesas() {
        List<Despesa> despesas = despesaService.getAllDespesas();
        List<DespesaDTO> despesaDTOs = despesas.stream()
                .map(d -> new DespesaDTO(
                        d.getId(),
                        d.getUsuario().getIdUsuario(),
                        d.getDescricao(),
                        d.getValor(),
                        d.getCategoriaDespesa() != null ? d.getCategoriaDespesa().getNome() : null,
                        d.getTipo(),
                        d.getData()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(despesaDTOs);
    }

    /**
     * Retorna uma despesa específica com base no ID fornecido.
     *
     * @param id O UUID da despesa a ser buscada.
     * @return ResponseEntity contendo o DespesaDTO correspondente ou 404 se não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> getDespesaById(@PathVariable UUID id) {
        Optional<Despesa> despesa = despesaService.getDespesaById(id);
        return despesa.map(d -> new DespesaDTO(
                        d.getId(),
                        d.getUsuario().getIdUsuario(),
                        d.getDescricao(),
                        d.getValor(),
                        d.getCategoriaDespesa() != null ? d.getCategoriaDespesa().getNome() : null,
                        d.getTipo(),
                        d.getData()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    /**
     * Retorna uma lista de despesas de um usuário específico.
     *
     * @param usuarioId O UUID do usuário cujas despesas serão buscadas.
     * @return ResponseEntity contendo uma lista de DespesaDTO com as despesas do usuário.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DespesaDTO>> getDespesasByUsuarioId(@PathVariable UUID usuarioId) {
        Usuario usuario = usuarioService.getUsuarioById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        List<Despesa> despesas = despesaService.getDespesasByUsuarioId(usuarioId);
        List<DespesaDTO> despesaDTOs = despesas.stream()
                .map(d -> new DespesaDTO(
                        d.getId(),
                        d.getUsuario().getIdUsuario(),
                        d.getDescricao(),
                        d.getValor(),
                        d.getCategoriaDespesa() != null ? d.getCategoriaDespesa().getNome() : null,
                        d.getTipo(),
                        d.getData()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(despesaDTOs);
    }

    /**
     * Cria uma nova despesa com os dados fornecidos.
     *
     * @param despesaDTO O DTO contendo os dados da nova despesa.
     * @return ResponseEntity com a despesa criada ou uma mensagem de erro se a criação falhar.
     */
    @PostMapping
    public ResponseEntity<DespesaDTO> createDespesa(@RequestBody DespesaDTO despesaDTO) {
        if (despesaDTO.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Usuario usuario = usuarioService.getUsuarioById(despesaDTO.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        CategoriaDespesa categoria = categoriaDespesaService.findByNome(despesaDTO.getCategoriaDespesaNome())
                .orElseGet(() -> {
                    CategoriaDespesa novaCategoria = new CategoriaDespesa();
                    novaCategoria.setNome(despesaDTO.getCategoriaDespesaNome());
                    return categoriaDespesaService.save(novaCategoria);
                });

        Despesa despesa = new Despesa();
        despesa.setUsuario(usuario);
        despesa.setDescricao(despesaDTO.getDescricao());
        despesa.setValor(despesaDTO.getValor());
        despesa.setCategoriaDespesa(categoria);
        despesa.setTipo(despesaDTO.getTipo());
        despesa.setData(despesaDTO.getData());

        Despesa createdDespesa = despesaService.saveDespesa(despesa);

        DespesaDTO dto = new DespesaDTO(
                createdDespesa.getId(),
                createdDespesa.getUsuario().getIdUsuario(),
                createdDespesa.getDescricao(),
                createdDespesa.getValor(),
                createdDespesa.getCategoriaDespesa() != null ? createdDespesa.getCategoriaDespesa().getNome() : null,
                createdDespesa.getTipo(),
                createdDespesa.getData()
        );
        return ResponseEntity.status(201).body(dto);
    }

    /**
     * Atualiza uma despesa existente com os dados fornecidos.
     *
     * @param id O UUID da despesa a ser atualizada.
     * @param despesaDTO O DTO contendo os dados atualizados da despesa.
     * @return ResponseEntity com a despesa atualizada ou 404 se a despesa não for encontrada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DespesaDTO> updateDespesa(@PathVariable UUID id, @RequestBody DespesaDTO despesaDTO) {
        Optional<Despesa> existingDespesa = despesaService.getDespesaById(id);
        if (existingDespesa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioService.getUsuarioById(despesaDTO.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        CategoriaDespesa categoria = categoriaDespesaService.findByNome(despesaDTO.getCategoriaDespesaNome())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria de despesa não encontrada"));

        Despesa despesa = existingDespesa.get();
        despesa.setDescricao(despesaDTO.getDescricao());
        despesa.setValor(despesaDTO.getValor());
        despesa.setCategoriaDespesa(categoria);
        despesa.setTipo(despesaDTO.getTipo());
        despesa.setData(despesaDTO.getData());

        Despesa updatedDespesa = despesaService.updateDespesa(id, despesa);

        DespesaDTO dto = new DespesaDTO(
                updatedDespesa.getId(),
                updatedDespesa.getUsuario().getIdUsuario(),
                updatedDespesa.getDescricao(),
                updatedDespesa.getValor(),
                updatedDespesa.getCategoriaDespesa() != null ? updatedDespesa.getCategoriaDespesa().getNome() : null,
                updatedDespesa.getTipo(),
                updatedDespesa.getData()
        );
        return ResponseEntity.ok(dto);
    }

    /**
     * Deleta uma despesa existente com base no ID fornecido.
     *
     * @param id O UUID da despesa a ser deletada.
     * @return ResponseEntity com status 204 se a despesa foi deletada com sucesso, ou 404 se não foi encontrada.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDespesa(@PathVariable UUID id) {
        Optional<Despesa> existingDespesa = despesaService.getDespesaById(id);
        if (existingDespesa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        despesaService.deleteDespesa(id);
        return ResponseEntity.noContent().build();
    }
}
