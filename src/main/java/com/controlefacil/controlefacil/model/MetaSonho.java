package com.controlefacil.controlefacil.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Representa uma Meta no sistema.
 */
@Data
@Entity
@Table(name = "meta_sonhos")
@NoArgsConstructor
@AllArgsConstructor
public class MetaSonho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Column(name = "valor_alvo", nullable = false, precision = 38, scale = 2)
    private BigDecimal valorAlvo;

    @Column(name = "valor_total", nullable = false, precision = 38, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_economizado", nullable = false, precision = 38, scale = 2)
    private BigDecimal valorEconomizado = BigDecimal.ZERO;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime prazo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ATIVA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public MetaSonho(UUID id) {
        this.id = id;
    }
}
