package com.controlefacil.controlefacil.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
/**
 * Representa uma Previsao Gastos no sistema.
 */
@Data
@Entity
@Table(name = "previsao_gastos")
public class PrevisaoGastos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "limite_gastos", nullable = false)
    private BigDecimal limiteGastos;

    @Column(name = "gastos_atuais", nullable = false)
    private BigDecimal gastosAtuais = BigDecimal.ZERO;

    @Column(name = "data_revisao", nullable = false)
    private LocalDate dataRevisao;
}
