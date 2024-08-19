package com.controlefacil.controlefacil.service;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario user) {
        return usuarioRepository.save(user);
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
