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

/**
 * Serviço responsável pela gestão das rendas dos usuários.
 * Este serviço permite criar, atualizar, obter e deletar rendas associadas a usuários.
 */
@Service
public class RendaService {

    @Autowired
    private RendaRepository rendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Retorna todas as rendas cadastradas.
     *
     * @return Uma lista com todas as rendas.
     */
    public List<Renda> getAllRendas() {
        return rendaRepository.findAll();
    }

    /**
     * Obtém uma renda pelo seu ID.
     *
     * @param id O ID da renda a ser buscada.
     * @return Um objeto Optional contendo a renda se encontrada, ou vazio se não.
     * @throws IllegalArgumentException Se o ID fornecido for nulo.
     */
    public Optional<Renda> getRendaById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda não pode ser nulo.");
        }
        return rendaRepository.findById(id);
    }

    /**
     * Salva uma nova renda.
     *
     * @param renda O objeto Renda a ser salvo.
     * @return A renda salva.
     * @throws IllegalArgumentException Se o ID do usuário não for fornecido.
     * @throws RecursoNaoEncontradoException Se o usuário associado à renda não for encontrado.
     */
    public Renda saveRenda(Renda renda) {
        if (renda.getUsuario() == null || renda.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("O ID do usuário deve ser fornecido.");
        }
        Usuario usuario = usuarioRepository.findById(renda.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o id " + renda.getUsuario().getIdUsuario()));
        renda.setUsuario(usuario);
        return rendaRepository.save(renda);
    }

    /**
     * Atualiza uma renda existente.
     *
     * @param id O ID da renda a ser atualizada.
     * @param rendaDetails Objeto com os novos detalhes da renda.
     * @return A renda atualizada.
     * @throws IllegalArgumentException Se o ID fornecido for nulo.
     * @throws RecursoNaoEncontradoException Se a renda não for encontrada.
     */
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
    }/**
     * Obtém todas as rendas associadas a um usuário específico.
     *
     * @param usuarioId O ID do usuário cujas rendas devem ser buscadas.
     * @return Uma lista de rendas associadas ao usuário.
     */
    public List<Renda> getRendasByUsuarioId(UUID usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("O ID do usuário não pode ser nulo.");
        }
        return rendaRepository.findByUsuario_IdUsuario(usuarioId);
    }

    /**
     * Deleta uma renda pelo seu ID.
     *
     * @param id O ID da renda a ser deletada.
     * @throws IllegalArgumentException Se o ID fornecido for nulo.
     * @throws RecursoNaoEncontradoException Se a renda não for encontrada.
     */
    public void deleteRenda(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda deve ser fornecido.");
        }
        Renda renda = getRendaById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Renda não encontrada com o id " + id));
        rendaRepository.delete(renda);
    }
}
