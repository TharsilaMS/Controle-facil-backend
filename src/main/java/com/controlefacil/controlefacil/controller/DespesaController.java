package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.DespesaDTO;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping
    public ResponseEntity<List<DespesaDTO>> getAllDespesas() {
        List<Despesa> despesas = despesaService.getAllDespesas();
        List<DespesaDTO> despesaDTOs = despesas.stream()
                .map(d -> new DespesaDTO(
                        d.getId(),
                        d.getUsuario().getIdUsuario(),
                        d.getDescricao(),
                        d.getValor(),
                        d.getCategoriaDespesa() != null ? d.getCategoriaDespesa().getId() : null,
                        d.getTipo(),
                        d.getData()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(despesaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> getDespesaById(@PathVariable Long id) {
        Optional<Despesa> despesa = despesaService.getDespesaById(id);
        return despesa.map(d -> new DespesaDTO(
                        d.getId(),
                        d.getUsuario().getIdUsuario(),
                        d.getDescricao(),
                        d.getValor(),
                        d.getCategoriaDespesa() != null ? d.getCategoriaDespesa().getId() : null,
                        d.getTipo(),
                        d.getData()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DespesaDTO> createDespesa(@RequestBody DespesaDTO despesaDTO) {
        try {
            Despesa despesa = new Despesa();
            despesa.setUsuario(new Usuario(despesaDTO.getUsuarioId())); // Ajuste conforme o construtor adequado
            despesa.setDescricao(despesaDTO.getDescricao());
            despesa.setValor(despesaDTO.getValor());

            CategoriaDespesa categoriaDespesa = new CategoriaDespesa();
            categoriaDespesa.setId(despesaDTO.getCategoriaDespesaId()); // Usando setters
            despesa.setCategoriaDespesa(categoriaDespesa);

            despesa.setTipo(despesaDTO.getTipo());  // Corrigido
            despesa.setData(despesaDTO.getData());

            Despesa savedDespesa = despesaService.saveDespesa(despesa);
            DespesaDTO dto = new DespesaDTO(
                    savedDespesa.getId(),
                    savedDespesa.getUsuario().getIdUsuario(),
                    savedDespesa.getDescricao(),
                    savedDespesa.getValor(),
                    savedDespesa.getCategoriaDespesa() != null ? savedDespesa.getCategoriaDespesa().getId() : null,
                    savedDespesa.getTipo(),  // Corrigido
                    savedDespesa.getData()
            );
            return ResponseEntity.status(201).body(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDTO> updateDespesa(@PathVariable Long id, @RequestBody DespesaDTO despesaDTO) {
        if (despesaService.getDespesaById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Despesa despesa = new Despesa();
        despesa.setId(id);
        despesa.setUsuario(new Usuario(despesaDTO.getUsuarioId())); // Ajuste conforme o construtor adequado
        despesa.setDescricao(despesaDTO.getDescricao());
        despesa.setValor(despesaDTO.getValor());

        CategoriaDespesa categoriaDespesa = new CategoriaDespesa();
        categoriaDespesa.setId(despesaDTO.getCategoriaDespesaId()); // Usando setters
        despesa.setCategoriaDespesa(categoriaDespesa);

        despesa.setTipo(despesaDTO.getTipo());  // Corrigido
        despesa.setData(despesaDTO.getData());

        Despesa updatedDespesa = despesaService.saveDespesa(despesa);
        DespesaDTO dto = new DespesaDTO(
                updatedDespesa.getId(),
                updatedDespesa.getUsuario().getIdUsuario(),
                updatedDespesa.getDescricao(),
                updatedDespesa.getValor(),
                updatedDespesa.getCategoriaDespesa() != null ? updatedDespesa.getCategoriaDespesa().getId() : null,
                updatedDespesa.getTipo(),  // Corrigido
                updatedDespesa.getData()
        );
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDespesa(@PathVariable Long id) {
        if (despesaService.getDespesaById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        despesaService.deleteDespesa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DespesaDTO>> getDespesasByUsuarioId(@PathVariable Long usuarioId) {
        List<Despesa> despesas = despesaService.getDespesasByUsuarioId(usuarioId);
        List<DespesaDTO> despesaDTOs = despesas.stream()
                .map(d -> new DespesaDTO(
                        d.getId(),
                        d.getUsuario().getIdUsuario(),
                        d.getDescricao(),
                        d.getValor(),
                        d.getCategoriaDespesa() != null ? d.getCategoriaDespesa().getId() : null,
                        d.getTipo(),
                        d.getData()
                ))
                .collect(Collectors.toList());
        return despesas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(despesaDTOs);
    }
}

