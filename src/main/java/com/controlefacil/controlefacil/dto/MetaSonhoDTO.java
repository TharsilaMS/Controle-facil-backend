package com.controlefacil.controlefacil.dto;

import com.controlefacil.controlefacil.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaSonhoDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private BigDecimal valorAlvo;
    private BigDecimal valorEconomizado;
    private String prazo;
    private Status status;
    private Long usuarioId;
}

