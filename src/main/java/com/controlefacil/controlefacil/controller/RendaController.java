package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.service.RendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendas")
public class RendaController {

    @Autowired
    private RendaService rendaService;

    @GetMapping
    public List<Renda> getAllRendas() {
        return rendaService.getAllRendas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Renda> getRendaById(@PathVariable Long id) {
        Renda renda = rendaService.getRendaById(id);
        return ResponseEntity.ok(renda);
    }

    @PostMapping
    public ResponseEntity<Renda> createRenda(@RequestBody Renda renda) {
        if (renda.getUsuario() == null || renda.getUsuario().getIdUsuario() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Renda novaRenda = rendaService.saveRenda(renda);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaRenda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Renda> updateRenda(@PathVariable Long id, @RequestBody Renda rendaDetails) {
        Renda updatedRenda = rendaService.updateRenda(id, rendaDetails);
        return ResponseEntity.ok(updatedRenda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRenda(@PathVariable Long id) {
        rendaService.deleteRenda(id);
        return ResponseEntity.noContent().build();
    }
}
