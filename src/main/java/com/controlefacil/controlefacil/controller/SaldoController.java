package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.exception.ResourceNotFoundException;
import com.controlefacil.controlefacil.model.Saldo;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.SaldoService;
import com.controlefacil.controlefacil.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saldos")
public class SaldoController {
    @Autowired
    private SaldoService saldoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Saldo> getAllSaldos() {
        return saldoService.getAllSaldos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Saldo> getSaldoById(@PathVariable Long id) {
        return ResponseEntity.ok(saldoService.getSaldoById(id));
    }

    @PostMapping
    public ResponseEntity<Saldo> createSaldo(@RequestBody Saldo saldo) {
        if (saldo.getUsuario() == null || saldo.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("Usuario must not be null");
        }

        Usuario usuario = usuarioService.getUsuarioById(saldo.getUsuario().getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        saldo.setUsuario(usuario);
        Saldo savedSaldo = saldoService.saveSaldo(saldo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSaldo);
    }




    @PutMapping("/{id}")
    public ResponseEntity<Saldo> updateSaldo(@PathVariable Long id, @RequestBody Saldo saldo) {
        Saldo existingSaldo = saldoService.getSaldoById(id);
        existingSaldo.setSaldo(saldo.getSaldo());
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
