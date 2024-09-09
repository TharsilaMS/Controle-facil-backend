package com.controlefacil.controlefacil.dto;

import com.controlefacil.controlefacil.model.Tipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DespesaDTO {

    private UUID id;
    private UUID usuarioId;
    private String descricao;
    private BigDecimal valor;
    private String categoriaDespesaNome;;
    private Tipo tipo;
    private LocalDate data;
}
