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

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaDespesaService categoriaDespesaService;

    @GetMapping
    public ResponseEntity<List<DespesaDTO>> getAllDespesas() {
        List<Despesa> despesas = despesaService.getAllDespesas();

        List<DespesaDTO> despesaDTOs = despesas.stream()
                .map(d -> new DespesaDTO(
                        d.getId(),
                        d.getUsuario().getIdUsuario(),
                        d.getDescricao(),
                        d.getValor(),
                        d.getCategoriaDespesa() != null ? d.getCategoriaDespesa().getNome() : null, // Nome da categoria
                        d.getTipo(),
                        d.getData()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(despesaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> getDespesaById(@PathVariable UUID id) {
        Optional<Despesa> despesa = despesaService.getDespesaById(id);

        return despesa.map(d -> new DespesaDTO(
                        d.getId(),
                        d.getUsuario().getIdUsuario(),
                        d.getDescricao(),
                        d.getValor(),
                        d.getCategoriaDespesa() != null ? d.getCategoriaDespesa().getNome() : null, // Nome da categoria
                        d.getTipo(),
                        d.getData()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DespesaDTO> createDespesa(@RequestBody DespesaDTO despesaDTO) {
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
