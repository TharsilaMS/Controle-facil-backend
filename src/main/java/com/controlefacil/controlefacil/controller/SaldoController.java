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
import java.util.UUID;

/**
 * Controlador responsável por gerenciar os saldos dos usuários.
 * Este controlador oferece endpoints para calcular o saldo e recuperar despesas e rendas associadas a um usuário.
 */
@RestController
@RequestMapping("/api/saldos")
public class SaldoController {

    @Autowired
    private SaldoService saldoService;

    /**
     * Calcula e retorna o saldo de um usuário específico.
     *
     * @param usuarioId ID do usuário cujo saldo será calculado
     * @return Um objeto SaldoDTO contendo o ID do saldo, o ID do usuário e o saldo calculado
     */
    @GetMapping("/{usuarioId}")
    public ResponseEntity<SaldoDTO> getSaldo(@PathVariable UUID usuarioId) {
        BigDecimal saldo = saldoService.calcularSaldo(usuarioId);
        return ResponseEntity.ok(new SaldoDTO(null, usuarioId, saldo));
    }

    /**
     * Recupera todas as despesas de um usuário específico.
     *
     * @param usuarioId ID do usuário cujas despesas serão recuperadas
     * @return Uma lista de objetos Despesa associada ao usuário
     */
    @GetMapping("/despesas/{usuarioId}")
    public ResponseEntity<List<Despesa>> getDespesasByUsuario(@PathVariable UUID usuarioId) {
        List<Despesa> despesas = saldoService.getDespesasByUsuario(usuarioId);
        return ResponseEntity.ok(despesas);
    }

    /**
     * Recupera todas as rendas de um usuário específico.
     *
     * @param usuarioId ID do usuário cujas rendas serão recuperadas
     * @return Uma lista de objetos Renda associada ao usuário
     */
    @GetMapping("/rendas/{usuarioId}")
    public ResponseEntity<List<Renda>> getRendasByUsuario(@PathVariable UUID usuarioId) {
        List<Renda> rendas = saldoService.getRendasByUsuario(usuarioId);
        return ResponseEntity.ok(rendas);
    }
}
