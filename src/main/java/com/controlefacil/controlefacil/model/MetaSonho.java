package com.controlefacil.controlefacil.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "meta_sonhos")
@NoArgsConstructor
@AllArgsConstructor
public class MetaSonho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da meta
    @Column(nullable = false, length = 255)
    private String titulo;

    // Descrição da meta
    @Column(length = 500)
    private String descricao;

    // Valor alvo da meta
    @Column(name = "valor_alvo", nullable = false, precision = 38, scale = 2)
    private BigDecimal valorAlvo;

    // Valor total da meta
    @Column(name = "valor_total", nullable = false, precision = 38, scale = 2)
    private BigDecimal valorTotal;

    // Valor já economizado
    @Column(name = "valor_economizado", nullable = false, precision = 38, scale = 2)
    private BigDecimal valorEconomizado = BigDecimal.ZERO;

    // Data de criação
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    // Prazo para concluir a meta
    @Column(nullable = false)
    private LocalDateTime prazo;

    // Status da meta (Ativa, Concluída, Cancelada)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ATIVA;

    // Relação com o usuário (usuario_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Construtor com apenas o ID (para facilitar)
    public MetaSonho(Long id) {
        this.id = id;
    }


}