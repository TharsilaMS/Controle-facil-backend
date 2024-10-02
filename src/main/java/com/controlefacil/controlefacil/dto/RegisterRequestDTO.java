package com.controlefacil.controlefacil.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class RegisterRequestDTO {
    private String nome;
    private String email;
    private String senha;


}
