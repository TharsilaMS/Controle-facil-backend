package com.controlefacil.controlefacil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meta_sonhos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaSonhos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private String titulo;

    @Lob
    private String descricao;

    @Column(name = "valor_alvo", nullable = false)
    private Double valorAlvo;

    @Column(nullable = false)
    private LocalDate prazo;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;
}

