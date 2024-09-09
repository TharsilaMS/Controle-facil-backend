package com.controlefacil.controlefacil.repository;


import com.controlefacil.controlefacil.model.MetaSonho;
import com.controlefacil.controlefacil.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetaSonhosRepository extends JpaRepository<MetaSonho, UUID> {
    List<MetaSonho> findByUsuario_IdUsuario(UUID idUsuario);

    Optional<MetaSonho> findByIdAndUsuario_IdUsuario(UUID id, UUID idUsuario);

    List<MetaSonho> findByUsuario_IdUsuarioAndStatus(UUID usuarioId, Status status);
}



