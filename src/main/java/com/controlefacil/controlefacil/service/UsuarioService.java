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

/**
 * Serviço que gerencia as operações relacionadas aos usuários.
 * Este serviço permite criar, atualizar, excluir e recuperar informações sobre os usuários
 * e também inicializa um saldo para cada novo usuário criado.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    /**
     * Retorna uma lista de todos os usuários cadastrados.
     *
     * @return Uma lista com todos os usuários.
     */
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id O ID do usuário que se deseja buscar.
     * @return Um objeto Optional contendo o usuário, se encontrado.
     */
    public Optional<Usuario> getUsuarioById(UUID id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Cria um novo usuário e inicializa seu saldo com zero.
     *
     * @param user O usuário a ser criado.
     * @return O usuário criado com o ID gerado.
     */
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

    /**
     * Atualiza as informações de um usuário existente.
     *
     * @param id O ID do usuário a ser atualizado.
     * @param user O novo usuário com os dados atualizados.
     * @return O usuário atualizado, ou null se o ID não for encontrado.
     */
    public Usuario updateUsuario(UUID id, Usuario user) {
        if (usuarioRepository.existsById(id)) {
            user.setIdUsuario(id);
            return usuarioRepository.save(user);
        }
        return null;
    }

    /**
     * Remove um usuário pelo seu ID.
     *
     * @param id O ID do usuário a ser removido.
     */
    public void deleteUsuario(UUID id) {
        usuarioRepository.deleteById(id);
    }
}
