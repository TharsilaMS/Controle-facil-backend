package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.ResourceNotFoundException;
import com.controlefacil.controlefacil.model.Renda;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.RendaRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RendaService {

    @Autowired
    private RendaRepository rendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Renda> getAllRendas() {
        return rendaRepository.findAll();
    }

    public Renda getRendaById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda não pode ser nulo.");
        }
        return rendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Renda não encontrada com o id " + id));
    }

    public Renda saveRenda(Renda renda) {
        if (renda.getUsuario() == null || renda.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("O ID do usuário deve ser fornecido.");
        }
        Usuario usuario = usuarioRepository.findById(renda.getUsuario().getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id " + renda.getUsuario().getIdUsuario()));
        renda.setUsuario(usuario);
        return rendaRepository.save(renda);
    }

    public Renda updateRenda(Long id, Renda rendaDetails) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda deve ser fornecido.");
        }
        Renda renda = getRendaById(id);
        renda.setDescricao(rendaDetails.getDescricao());
        renda.setValor(rendaDetails.getValor());
        renda.setData(rendaDetails.getData());

        if (rendaDetails.getUsuario() != null && rendaDetails.getUsuario().getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(rendaDetails.getUsuario().getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id " + rendaDetails.getUsuario().getIdUsuario()));
            renda.setUsuario(usuario);
        }

        return rendaRepository.save(renda);
    }

    public void deleteRenda(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda deve ser fornecido.");
        }
        Renda renda = getRendaById(id);
        rendaRepository.delete(renda);
    }
}
