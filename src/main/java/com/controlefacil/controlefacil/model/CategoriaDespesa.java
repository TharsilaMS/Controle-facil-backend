package com.controlefacil.controlefacil.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "categoria_despesa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDespesa {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;


        @Column(nullable = false)
        private String nome;
        public CategoriaDespesa(UUID id) {
                this.id = id;
        }
    }


