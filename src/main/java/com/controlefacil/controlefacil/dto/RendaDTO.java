package com.controlefacil.controlefacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendaDTO {

    private Long id;
    private Long usuarioId;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
}
