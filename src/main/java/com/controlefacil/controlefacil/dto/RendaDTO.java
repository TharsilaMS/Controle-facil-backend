package com.controlefacil.controlefacil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para Renda.
 */
@Setter
@Getter
@AllArgsConstructor
public class RendaDTO {
    private UUID id;
    private UUID usuarioId;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

}
