package com.controlefacil.controlefacil.repository;


import com.controlefacil.controlefacil.model.MetaSonho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MetaSonhosRepository extends JpaRepository<MetaSonho, Long> {
    List<MetaSonho> findByUsuario_IdUsuario(Long idUsuario);

    Optional<MetaSonho> findByIdAndUsuario_IdUsuario(Long id, Long idUsuario);
}



