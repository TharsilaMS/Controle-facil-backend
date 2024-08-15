package com.controlefacil.controlefacil.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}