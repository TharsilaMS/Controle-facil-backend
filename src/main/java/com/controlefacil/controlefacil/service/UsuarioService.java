package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.Saldo;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.SaldoRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(UUID id) {
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario user) {
        Usuario novoUsuario = usuarioRepository.save(user);
        Saldo saldoInicial = new Saldo();
        saldoInicial.setUsuario(novoUsuario);
        saldoInicial.setSaldo(BigDecimal.ZERO);
        saldoInicial.setData(LocalDate.now());
        saldoInicial.setDescricao("Saldo inicial");
        saldoRepository.save(saldoInicial);

        return novoUsuario;
    }

    public Usuario updateUsuario(UUID id, Usuario user) {
        if (usuarioRepository.existsById(id)) {
            user.setIdUsuario(id);
            return usuarioRepository.save(user);
        }
        return null;
    }

    public void deleteUsuario(UUID id) {
        usuarioRepository.deleteById(id);
    }
}
