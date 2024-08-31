package com.controlefacil.controlefacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrevisaoGastosDTO {

    private Long id;
    private Long usuarioId;
    private BigDecimal limiteGastos;
    private BigDecimal gastosAtuais;
    private LocalDate dataRevisao;
}
