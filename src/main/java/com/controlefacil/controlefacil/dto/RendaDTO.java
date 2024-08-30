package com.controlefacil.controlefacil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class RendaDTO {
    private Long id;
    private Long usuarioId;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

}
