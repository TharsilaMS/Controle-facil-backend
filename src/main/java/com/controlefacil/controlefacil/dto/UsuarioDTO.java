package com.controlefacil.controlefacil.dto;

import com.controlefacil.controlefacil.model.FaixaSalarial;
import com.controlefacil.controlefacil.model.Genero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Genero genero; // Alterado para usar o Enum
    private LocalDate dataNascimento;
    private String ramoAtuacao;
    private FaixaSalarial faixaSalarial; // Alterado para usar o Enum

}
