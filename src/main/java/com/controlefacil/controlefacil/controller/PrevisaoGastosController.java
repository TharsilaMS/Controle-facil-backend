package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.PrevisaoGastosDTO;
import com.controlefacil.controlefacil.model.PrevisaoGastos;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.PrevisaoGastosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/previsao-gastos")
public class PrevisaoGastosController {

    @Autowired
    private PrevisaoGastosService previsaoGastosService;

    @PostMapping
    public ResponseEntity<PrevisaoGastosDTO> createPrevisaoGastos(@RequestBody PrevisaoGastosDTO previsaoGastosDTO) {
        try {
            // Criação do objeto PrevisaoGastos a partir do DTO
            PrevisaoGastos previsaoGastos = new PrevisaoGastos();
            previsaoGastos.setUsuario(new Usuario(previsaoGastosDTO.getUsuarioId()));
            previsaoGastos.setLimiteGastos(previsaoGastosDTO.getLimiteGastos());
            previsaoGastos.setDataRevisao(previsaoGastosDTO.getDataRevisao());

            // Salvando a previsão de gastos
            PrevisaoGastos savedPrevisaoGastos = previsaoGastosService.createPrevisaoGastos(previsaoGastos);

            // Criando o DTO para a resposta
            PrevisaoGastosDTO dto = new PrevisaoGastosDTO(
                    savedPrevisaoGastos.getId(),
                    savedPrevisaoGastos.getUsuario().getIdUsuario(),
                    savedPrevisaoGastos.getLimiteGastos(),
                    savedPrevisaoGastos.getGastosAtuais(),
                    savedPrevisaoGastos.getDataRevisao()
            );

            // Retornando a resposta com status 201 Created
            return ResponseEntity.status(201).body(dto);
        } catch (Exception e) {
            // Retornando status 500 Internal Server Error em caso de exceção
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


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
