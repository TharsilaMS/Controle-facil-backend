package com.controlefacil.controlefacil.repository;


import com.controlefacil.controlefacil.model.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
/**
 * Repositório para gerenciar os saldos.
 */
public interface SaldoRepository extends JpaRepository<Saldo, UUID> {

}