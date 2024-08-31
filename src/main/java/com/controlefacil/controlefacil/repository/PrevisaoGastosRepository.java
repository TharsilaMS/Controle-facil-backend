package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.PrevisaoGastos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrevisaoGastosRepository extends JpaRepository<PrevisaoGastos, Long> {

    Optional<PrevisaoGastos> findByUsuario_IdUsuarioAndDataRevisao(Long usuarioId, LocalDate dataRevisao);

    List<PrevisaoGastos>  findByUsuario_IdUsuario(Long idUsuario);
}
