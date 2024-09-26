package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.PrevisaoGastosDTO;
import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.PrevisaoGastosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador responsável por gerenciar as previsões de gastos dos usuários.
 * Este controlador permite criar, atualizar e obter previsões de gastos.
 */
@RestController
@RequestMapping("/api/previsao-gastos")
public class PrevisaoGastosController {

    @Autowired
    private PrevisaoGastosService previsaoGastosService;

    /**
     * Cria uma nova previsão de gastos para um usuário.
     *
     * @param previsaoGastosDTO Dados da previsão de gastos a ser criada
     * @return A previsão de gastos criada, com status de resposta "Criado"
     */
    @PostMapping
    public ResponseEntity<PrevisaoGastosDTO> createPrevisaoGastos(@RequestBody PrevisaoGastosDTO previsaoGastosDTO) {
        try {
            PrevisaoGastos previsaoGastos = new PrevisaoGastos();
            previsaoGastos.setUsuario(new Usuario(previsaoGastosDTO.getUsuarioId()));
            previsaoGastos.setLimiteGastos(previsaoGastosDTO.getLimiteGastos());
            previsaoGastos.setDataRevisao(previsaoGastosDTO.getDataRevisao());

            PrevisaoGastos savedPrevisaoGastos = previsaoGastosService.createPrevisaoGastos(previsaoGastos);
            PrevisaoGastosDTO dto = new PrevisaoGastosDTO(
                    savedPrevisaoGastos.getId(),
                    savedPrevisaoGastos.getUsuario().getIdUsuario(),
                    savedPrevisaoGastos.getLimiteGastos(),
                    savedPrevisaoGastos.getGastosAtuais(),
                    savedPrevisaoGastos.getDataRevisao()
            );
            return ResponseEntity.status(201).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Atualiza a previsão de gastos de um usuário específico.
     *
     * @param usuarioId ID do usuário cuja previsão de gastos será atualizada
     * @param previsaoGastosDTO Dados atualizados da previsão de gastos
     * @return A previsão de gastos atualizada, ou uma resposta "não encontrado" se não existir
     */
    @PutMapping("/{usuarioId}")
    public ResponseEntity<PrevisaoGastosDTO> updatePrevisaoGastos(@PathVariable UUID usuarioId, @RequestBody PrevisaoGastosDTO previsaoGastosDTO) {
        try {
            PrevisaoGastos previsaoGastos = new PrevisaoGastos();
            previsaoGastos.setLimiteGastos(previsaoGastosDTO.getLimiteGastos());
            previsaoGastos.setDataRevisao(previsaoGastosDTO.getDataRevisao());

            PrevisaoGastos updatedPrevisaoGastos = previsaoGastosService.updatePrevisaoGastos(usuarioId, previsaoGastos);
            PrevisaoGastosDTO dto = new PrevisaoGastosDTO(
                    updatedPrevisaoGastos.getId(),
                    updatedPrevisaoGastos.getUsuario().getIdUsuario(),
                    updatedPrevisaoGastos.getLimiteGastos(),
                    updatedPrevisaoGastos.getGastosAtuais(),
                    updatedPrevisaoGastos.getDataRevisao()
            );
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recupera a previsão de gastos de um usuário específico.
     *
     * @param usuarioId ID do usuário cuja previsão de gastos será obtida
     * @return A previsão de gastos do usuário, ou uma resposta "não encontrado" se não existir
     */
    @GetMapping("/{usuarioId}")
    public ResponseEntity<PrevisaoGastosDTO> getPrevisaoGastos(@PathVariable UUID usuarioId) {
        try {
            previsaoGastosService.updateGastosAtuais(usuarioId);
            previsaoGastosService.verificarLimite(usuarioId);

            PrevisaoGastos previsaoGastos = previsaoGastosService.getPrevisaoGastos(usuarioId);
            PrevisaoGastosDTO dto = new PrevisaoGastosDTO(
                    previsaoGastos.getId(),
                    previsaoGastos.getUsuario().getIdUsuario(),
                    previsaoGastos.getLimiteGastos(),
                    previsaoGastos.getGastosAtuais(),
                    previsaoGastos.getDataRevisao()
            );
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
