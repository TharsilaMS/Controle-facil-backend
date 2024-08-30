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

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/api/saldos")
public class SaldoController {

    @Autowired
    private SaldoService saldoService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<BigDecimal> getSaldo(@PathVariable Long usuarioId) {
        BigDecimal saldo = saldoService.calcularSaldo(usuarioId);
        return ResponseEntity.ok(saldo);
    }
}

