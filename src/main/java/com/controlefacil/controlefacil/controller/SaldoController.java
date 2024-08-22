package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.model.Saldo;
import com.controlefacil.controlefacil.service.SaldoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saldos")
public class SaldoController {
    @Autowired
    private SaldoService saldoService;

    @GetMapping
    public List<Saldo> getAllSaldos() {
        return saldoService.getAllSaldos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Saldo> getSaldoById(@PathVariable Long id) {
        return ResponseEntity.ok(saldoService.getSaldoById(id));
    }

    @PostMapping
    public Saldo createSaldo(@RequestBody Saldo saldo) {
        return saldoService.saveSaldo(saldo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Saldo> updateSaldo(@PathVariable Long id, @RequestBody Saldo saldo) {
        Saldo existingSaldo = saldoService.getSaldoById(id);
        existingSaldo.setValor(saldo.getValor());
        existingSaldo.setData(saldo.getData());
        existingSaldo.setDescricao(saldo.getDescricao());
        return ResponseEntity.ok(saldoService.saveSaldo(existingSaldo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaldo(@PathVariable Long id) {
        saldoService.deleteSaldo(id);
        return ResponseEntity.noContent().build();
    }
}

