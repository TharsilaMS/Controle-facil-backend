package com.controlefacil.controlefacil.models;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class FinancialGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private BigDecimal targetAmount;

    @Column(nullable = false)
    private BigDecimal currentAmount;

    @Column(nullable = false)
    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}