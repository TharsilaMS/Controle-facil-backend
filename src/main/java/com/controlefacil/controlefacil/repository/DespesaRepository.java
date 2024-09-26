package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
/**
 * Repositório para gerenciar as despesa.
 */
public interface DespesaRepository extends JpaRepository<Despesa, UUID> {

    List<Despesa> findByUsuario_IdUsuario(UUID idUsuario);


    }


