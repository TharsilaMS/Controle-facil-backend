package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.CategoriaDespesaDTO;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.service.CategoriaDespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador REST para gerenciar categorias de despesas.
 * Proporciona endpoints para CRUD de categorias de despesas.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaDespesaController {

    @Autowired
    private CategoriaDespesaService service;

    /**
     * Retorna todas as categorias de despesas disponíveis.
     *
     * @return ResponseEntity contendo uma lista de CategoriaDespesaDTO com todas as categorias.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaDespesaDTO>> getAllCategorias() {
        List<CategoriaDespesa> categorias = service.findAll();
        List<CategoriaDespesaDTO> categoriaDTOs = categorias.stream()
                .map(categoria -> new CategoriaDespesaDTO(categoria.getId(), categoria.getNome()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoriaDTOs);
    }

    /**
     * Retorna uma categoria de despesa específica com base no ID fornecido.
     *
     * @param id O UUID da categoria de despesa.
     * @return ResponseEntity contendo o CategoriaDespesaDTO correspondente ou 404 se não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDespesaDTO> getCategoriaById(@PathVariable UUID id) {
        Optional<CategoriaDespesa> categoria = service.findById(id);
        return categoria.map(c -> new CategoriaDespesaDTO(c.getId(), c.getNome()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova categoria de despesa.
     *
     * @param categoriaDTO O DTO contendo os dados da nova categoria.
     * @return ResponseEntity com a nova CategoriaDespesaDTO criada ou uma mensagem de erro se a criação falhar.
     */
    @PostMapping
    public ResponseEntity<CategoriaDespesaDTO> createCategoria(@RequestBody CategoriaDespesaDTO categoriaDTO) {
        try {
            CategoriaDespesa categoria = new CategoriaDespesa(null, categoriaDTO.getNome());
            CategoriaDespesa createdCategoria = service.save(categoria);
            CategoriaDespesaDTO dto = new CategoriaDespesaDTO(createdCategoria.getId(), createdCategoria.getNome());
            return ResponseEntity.status(201).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CategoriaDespesaDTO(null, e.getMessage()));
        }
    }

    /**
     * Atualiza uma categoria de despesa existente com base no ID fornecido.
     *
     * @param id O UUID da categoria de despesa a ser atualizada.
     * @param categoriaDTO O DTO contendo os dados atualizados da categoria.
     * @return ResponseEntity contendo o CategoriaDespesaDTO atualizado ou 404 se a categoria não for encontrada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDespesaDTO> updateCategoria(@PathVariable UUID id, @RequestBody CategoriaDespesaDTO categoriaDTO) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CategoriaDespesa categoria = new CategoriaDespesa(id, categoriaDTO.getNome());
        CategoriaDespesa updatedCategoria = service.save(categoria);
        CategoriaDespesaDTO dto = new CategoriaDespesaDTO(updatedCategoria.getId(), updatedCategoria.getNome());
        return ResponseEntity.ok(dto);
    }

}
