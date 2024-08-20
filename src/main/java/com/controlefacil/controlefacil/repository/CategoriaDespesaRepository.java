package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaDespesaRepository extends JpaRepository<CategoriaDespesa, Long> {
}
