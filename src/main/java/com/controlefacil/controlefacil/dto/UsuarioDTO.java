package com.controlefacil.controlefacil.dto;

import com.controlefacil.controlefacil.model.FaixaSalarial;
import com.controlefacil.controlefacil.model.Genero;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para Usuário.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private UUID idUsuario;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotNull(message = "Gênero é obrigatório")
    private Genero genero;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @NotBlank(message = "Ramo de atuação é obrigatório")
    private String ramoAtuacao;

    @NotNull(message = "Faixa salarial é obrigatória")
    private FaixaSalarial faixaSalarial;
}