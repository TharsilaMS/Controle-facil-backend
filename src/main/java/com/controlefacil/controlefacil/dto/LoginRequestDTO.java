package com.controlefacil.controlefacil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequestDTO {
    @Email(message = "Email não é valido")
    @NotBlank(message = "Email é obrigatorio")
    private String email;
    @NotBlank(message = "Senha é obrigatoria")
    private String senha;


}
