package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.RendaRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RendaService {

    @Autowired
    private RendaRepository rendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Renda> getAllRendas() {
        return rendaRepository.findAll();
    }

    public Optional<Renda> getRendaById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda não pode ser nulo.");
        }
        return rendaRepository.findById(id);
    }

    public Renda saveRenda(Renda renda) {
        if (renda.getUsuario() == null || renda.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("O ID do usuário deve ser fornecido.");
        }
        Usuario usuario = usuarioRepository.findById(renda.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o id " + renda.getUsuario().getIdUsuario()));
        renda.setUsuario(usuario);
        return rendaRepository.save(renda);
    }

    public Renda updateRenda(UUID id, Renda rendaDetails) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda deve ser fornecido.");
        }
        Renda renda = getRendaById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Renda não encontrada com o id " + id));
        renda.setDescricao(rendaDetails.getDescricao());
        renda.setValor(rendaDetails.getValor());
        renda.setData(rendaDetails.getData());

        if (rendaDetails.getUsuario() != null && rendaDetails.getUsuario().getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(rendaDetails.getUsuario().getIdUsuario())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o id " + rendaDetails.getUsuario().getIdUsuario()));
            renda.setUsuario(usuario);
        }

        return rendaRepository.save(renda);
    }

    public void deleteRenda(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda deve ser fornecido.");
        }
        Renda renda = getRendaById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Renda não encontrada com o id " + id));
        rendaRepository.delete(renda);
    }
}
