package com.controlefacil.controlefacil.repository;

import com.controlefacil.controlefacil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
/**
 * Repositório para gerenciar os usuarios.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario>  findByEmail(String email);
}

