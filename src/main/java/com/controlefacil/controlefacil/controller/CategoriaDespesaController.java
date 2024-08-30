package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.service.CategoriaDespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaDespesaController {

    @Autowired
    private CategoriaDespesaService service;

    @GetMapping
    public List<CategoriaDespesa> getAllCategorias() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDespesa> getCategoriaById(@PathVariable Long id) {
        Optional<CategoriaDespesa> categoria = service.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CategoriaDespesa createCategoria(@RequestBody CategoriaDespesa categoriaDespesa) {
        return service.save(categoriaDespesa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDespesa> updateCategoria(@PathVariable Long id, @RequestBody CategoriaDespesa categoriaDespesa) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoriaDespesa.setId(id);
        return ResponseEntity.ok(service.save(categoriaDespesa));
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
