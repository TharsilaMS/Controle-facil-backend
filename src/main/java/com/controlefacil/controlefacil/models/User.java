package com.controlefacil.controlefacil.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate birthDate;

    @Column(name = "ramo_atuacao", length = 255)
    private String fieldOfActivity;

    @Enumerated(EnumType.STRING)
    @Column(name = "faixa_salarial")
    private SalaryRange salaryRange;

}

