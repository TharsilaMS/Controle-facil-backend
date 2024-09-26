package com.controlefacil.controlefacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para Previsão Gastos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrevisaoGastosDTO {

    private UUID id;
    private UUID usuarioId;
    private BigDecimal limiteGastos;
    private BigDecimal gastosAtuais;
    private LocalDate dataRevisao;
}
