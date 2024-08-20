package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping
    public List<Despesa> getAllDespesas() {
        return despesaService.getAllDespesas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> getDespesaById(@PathVariable Long id) {
        Optional<Despesa> despesa = despesaService.getDespesaById(id);
        return despesa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Despesa> createDespesa(@RequestBody Despesa despesa) {
        try {
            Despesa savedDespesa = despesaService.saveDespesa(despesa);
            return ResponseEntity.ok(savedDespesa);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Despesa> updateDespesa(@PathVariable Long id, @RequestBody Despesa despesa) {
        if (!despesaService.getDespesaById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        despesa.setId(id);
        return ResponseEntity.ok(despesaService.saveDespesa(despesa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDespesa(@PathVariable Long id) {
        if (!despesaService.getDespesaById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        despesaService.deleteDespesa(id);
        return ResponseEntity.noContent().build();
    }
}
