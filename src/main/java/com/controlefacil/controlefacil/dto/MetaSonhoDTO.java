package com.controlefacil.controlefacil.dto;

import com.controlefacil.controlefacil.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaSonhoDTO {
    private UUID id;
    private String titulo;
    private String descricao;
    private BigDecimal valorAlvo;
    private BigDecimal valorEconomizado;
    private String prazo;
    private Status status;
    private UUID usuarioId;
}

