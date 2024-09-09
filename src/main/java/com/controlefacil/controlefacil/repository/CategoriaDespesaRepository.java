package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaDespesaRepository extends JpaRepository<CategoriaDespesa, UUID> {
    Optional<CategoriaDespesa> findByNome(String nome);

}
