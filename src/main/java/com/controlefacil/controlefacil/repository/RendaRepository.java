package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Renda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
/**
 * Repositório para gerenciar as rendas.
 */
public interface RendaRepository extends JpaRepository<Renda, UUID> {
    List<Renda> findByUsuario_IdUsuario(UUID idUsuario);
}


