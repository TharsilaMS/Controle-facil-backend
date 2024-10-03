package com.controlefacil.controlefacil.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
    @AllArgsConstructor
    public class ResponseDTO {
        private String nome;
        private String token;
        private UUID usuarioId;
    }


