package com.controlefacil.controlefacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) para Categoria de Despesa.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDespesaDTO {

    private UUID id;
    private String nome;
}
