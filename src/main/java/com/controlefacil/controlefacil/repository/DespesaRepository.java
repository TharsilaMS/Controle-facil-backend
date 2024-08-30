package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByUsuario_IdUsuario(Long idUsuario);

    }


