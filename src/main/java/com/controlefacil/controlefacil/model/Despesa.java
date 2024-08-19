package com.controlefacil.controlefacil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Table(name = "Despesa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "usuario_id")
        private Usuario usuario;

        @Column(nullable = false)
        private String descricao;

        @Column(nullable = false)
        private Double valor;

        @ManyToOne
        @JoinColumn(name = "categoria_id")
        private CategoriaDespesa categoriaDespesa;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Tipo tipo;

        @Column(nullable = false)
        private LocalDate data;




}
