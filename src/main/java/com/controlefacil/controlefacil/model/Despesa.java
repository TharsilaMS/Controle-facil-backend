package com.controlefacil.controlefacil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Representa uma despesa no sistema.
 */
@Entity
@Table(name = "despesa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

        @Column(nullable = false)
        private String descricao;

        @Column(nullable = false)
        private BigDecimal valor;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "categoria_id")
        private CategoriaDespesa categoriaDespesa;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Tipo tipo;

        @Column(nullable = false)
        private LocalDate data;
}
