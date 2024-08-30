package com.controlefacil.controlefacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime dataCadastro;
    private String genero;
    private LocalDate dataNascimento;
    private String ramoAtuacao;
    private String faixaSalarial;
}
