package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.CategoriaDespesaDTO;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.service.CategoriaDespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaDespesaController {

    @Autowired
    private CategoriaDespesaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaDespesaDTO>> getAllCategorias() {
        List<CategoriaDespesa> categorias = service.findAll();
        List<CategoriaDespesaDTO> categoriaDTOs = categorias.stream()
                .map(categoria -> new CategoriaDespesaDTO(categoria.getId(), categoria.getNome()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoriaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDespesaDTO> getCategoriaById(@PathVariable Long id) {
        Optional<CategoriaDespesa> categoria = service.findById(id);
        return categoria.map(c -> new CategoriaDespesaDTO(c.getId(), c.getNome()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaDespesaDTO> createCategoria(@RequestBody CategoriaDespesaDTO categoriaDTO) {
        CategoriaDespesa categoria = new CategoriaDespesa(null, categoriaDTO.getNome());
        CategoriaDespesa createdCategoria = service.save(categoria);
        CategoriaDespesaDTO dto = new CategoriaDespesaDTO(createdCategoria.getId(), createdCategoria.getNome());
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDespesaDTO> updateCategoria(@PathVariable Long id, @RequestBody CategoriaDespesaDTO categoriaDTO) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CategoriaDespesa categoria = new CategoriaDespesa(id, categoriaDTO.getNome());
        CategoriaDespesa updatedCategoria = service.save(categoria);
        CategoriaDespesaDTO dto = new CategoriaDespesaDTO(updatedCategoria.getId(), updatedCategoria.getNome());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
