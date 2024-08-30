package com.controlefacil.controlefacil.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RendaDTO {

    private Long id;
    private Long usuarioId;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

    public RendaDTO() {
    }

    public RendaDTO(Long id, Long usuarioId, String descricao, BigDecimal valor, LocalDate data) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
