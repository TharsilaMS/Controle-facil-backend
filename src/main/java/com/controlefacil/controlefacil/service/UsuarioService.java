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

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario user) {
        // Cria o usuário
        Usuario novoUsuario = usuarioRepository.save(user);

        // Define o saldo inicial como zero
        Saldo saldoInicial = new Saldo();
        saldoInicial.setUsuario(novoUsuario);
        saldoInicial.setSaldo(BigDecimal.ZERO);
        saldoInicial.setData(LocalDate.now());
        saldoInicial.setDescricao("Saldo inicial");

        // Salva o saldo inicial
        saldoRepository.save(saldoInicial);

        return novoUsuario;
    }

    public Usuario updateUsuario(Long id, Usuario user) {
        if (usuarioRepository.existsById(id)) {
            user.setIdUsuario(id);
            return usuarioRepository.save(user);
        }
        return null;
    }

    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
