package com.controlefacil.controlefacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaldoDTO {

    private Long id;
    private Long usuarioId;
    private BigDecimal saldo;
    private LocalDate data;
    private String descricao;
}
