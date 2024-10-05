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
 * Serviço responsável por gerenciar as operações relacionadas à entidade Renda.
 * Fornece métodos para criar, atualizar, deletar e buscar rendas no sistema.
 */
@Service
public class RendaService {

    @Autowired
    private RendaRepository rendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Recupera todas as rendas do sistema.
     *
     * @return uma lista de objetos Renda representando todas as rendas no sistema
     */
    public List<Renda> getAllRendas() {
        return rendaRepository.findAll();
    }

    /**
     * Recupera uma renda específica pelo seu ID.
     *
     * @param id o UUID da renda
     * @return um Optional contendo a Renda, se encontrada, ou um Optional vazio se não encontrada
     * @throws IllegalArgumentException se o ID for nulo
     */
    public Optional<Renda> getRendaById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da renda não pode ser nulo.");
        }
        return rendaRepository.findById(id);
    }

    /**
     * Salva uma nova renda no sistema.
     *
     * @param renda o objeto Renda a ser salvo
     * @return o objeto Renda salvo no sistema
     * @throws IllegalArgumentException se o usuário associado à renda não for fornecido ou não existir
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
     * Atualiza uma renda existente no sistema.
     *
     * @param id o UUID da renda a ser atualizada
     * @param rendaDetails os novos detalhes da renda
     * @return o objeto Renda atualizado
     * @throws IllegalArgumentException se o ID da renda for nulo
     * @throws RecursoNaoEncontradoException se a renda ou o usuário não forem encontrados
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
        renda.setTipo(rendaDetails.getTipo()); // Atualizando o tipo

        if (rendaDetails.getUsuario() != null && rendaDetails.getUsuario().getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(rendaDetails.getUsuario().getIdUsuario())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o id " + rendaDetails.getUsuario().getIdUsuario()));
            renda.setUsuario(usuario);
        }

        return rendaRepository.save(renda);
    }

    /**
     * Recupera todas as rendas de um usuário específico.
     *
     * @param usuarioId o UUID do usuário
     * @return uma lista de objetos Renda associados ao usuário
     * @throws IllegalArgumentException se o ID do usuário for nulo
     */
    public List<Renda> getRendasByUsuarioId(UUID usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("O ID do usuário não pode ser nulo.");
        }
        return rendaRepository.findByUsuario_IdUsuario(usuarioId);
    }

    /**
     * Deleta uma renda específica pelo seu ID.
     *
     * @param id o UUID da renda a ser deletada
     * @throws IllegalArgumentException se o ID da renda for nulo
     * @throws RecursoNaoEncontradoException se a renda não for encontrada
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
