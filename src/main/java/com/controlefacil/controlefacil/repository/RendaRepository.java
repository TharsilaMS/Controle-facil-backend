package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Renda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RendaRepository extends JpaRepository<Renda, Long> {
    List<Renda> findByUsuario_IdUsuario(Long idUsuario);
}


