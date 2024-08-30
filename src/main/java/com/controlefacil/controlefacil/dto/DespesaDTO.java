package com.controlefacil.controlefacil.dto;

import com.controlefacil.controlefacil.model.Tipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DespesaDTO {

    private Long id;
    private Long usuarioId;
    private String descricao;
    private BigDecimal valor;
    private Long categoriaDespesaId;
    private Tipo tipo;
    private LocalDate data;
}
