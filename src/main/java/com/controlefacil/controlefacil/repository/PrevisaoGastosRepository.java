package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.PrevisaoGastos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Repositório para gerenciar as previsão de gastos.
 */
public interface PrevisaoGastosRepository extends JpaRepository<PrevisaoGastos, UUID> {

    Optional<PrevisaoGastos> findByUsuario_IdUsuarioAndDataRevisao(UUID usuarioId, LocalDate dataRevisao);

    List<PrevisaoGastos>  findByUsuario_IdUsuario(UUID idUsuario);
}
