package com.controlefacil.controlefacil.models;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 255)
    private String description;
}