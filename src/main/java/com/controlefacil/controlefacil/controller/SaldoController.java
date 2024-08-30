package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.SaldoDTO;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.service.SaldoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/saldos")
public class SaldoController {

    @Autowired
    private SaldoService saldoService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<SaldoDTO> getSaldo(@PathVariable Long usuarioId) {
        BigDecimal saldo = saldoService.calcularSaldo(usuarioId);
        return ResponseEntity.ok(new SaldoDTO(null, usuarioId, saldo, null, null));
    }

    @GetMapping("/despesas/{usuarioId}")
    public ResponseEntity<List<Despesa>> getDespesasByUsuario(@PathVariable Long usuarioId) {
        List<Despesa> despesas = saldoService.getDespesasByUsuario(usuarioId);
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/rendas/{usuarioId}")
    public ResponseEntity<List<Renda>> getRendasByUsuario(@PathVariable Long usuarioId) {
        List<Renda> rendas = saldoService.getRendasByUsuario(usuarioId);
        return ResponseEntity.ok(rendas);
    }
}
