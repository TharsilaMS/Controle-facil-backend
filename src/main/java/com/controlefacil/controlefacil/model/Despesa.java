package com.controlefacil.controlefacil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "despesa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY) // Melhor usar LAZY para não carregar desnecessariamente
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

        @Column(nullable = false)
        private String descricao;

        @Column(nullable = false)
        private Double valor;

        @ManyToOne(fetch = FetchType.LAZY) // Melhor usar LAZY para não carregar desnecessariamente
        @JoinColumn(name = "categoria_id", nullable = false)
        private CategoriaDespesa categoriaDespesa;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Tipo tipo;

        @Column(nullable = false)
        private LocalDate data;
}
