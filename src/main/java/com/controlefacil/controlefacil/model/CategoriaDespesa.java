package com.controlefacil.controlefacil.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "CategoriaDespesa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDespesa {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String nome;
    }


