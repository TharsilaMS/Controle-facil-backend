package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.UsuarioDTO;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador que gerencia as operações relacionadas aos usuários.
 * Este controlador permite criar, atualizar, buscar e deletar usuários.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Recupera todos os usuários cadastrados.
     *
     * @return Uma lista de objetos UsuarioDTO representando todos os usuários.
     */
    @GetMapping
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioService.getAllUsuarios().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca um usuário específico pelo ID.
     *
     * @param id ID do usuário a ser buscado
     * @return Um ResponseEntity contendo o objeto UsuarioDTO se encontrado, ou uma resposta 404 se não encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable UUID id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo usuário.
     *
     * @param usuarioDTO Objeto contendo os dados do novo usuário
     * @return Um ResponseEntity contendo o objeto UsuarioDTO do usuário criado, com status 201 (Criado).
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setGenero(usuarioDTO.getGenero());
        usuario.setDataNascimento(usuarioDTO.getDataNascimento());
        usuario.setRamoAtuacao(usuarioDTO.getRamoAtuacao());
        usuario.setFaixaSalarial(usuarioDTO.getFaixaSalarial());

        Usuario createdUsuario = usuarioService.createUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdUsuario));
    }

    /**
     * Atualiza as informações de um usuário existente.
     *
     * @param id ID do usuário a ser atualizado
     * @param usuarioDTO Objeto contendo os novos dados do usuário
     * @return Um ResponseEntity contendo o objeto UsuarioDTO atualizado, ou uma resposta 404 se o usuário não for encontrado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable UUID id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setGenero(usuarioDTO.getGenero());
        usuario.setDataNascimento(usuarioDTO.getDataNascimento());
        usuario.setRamoAtuacao(usuarioDTO.getRamoAtuacao());
        usuario.setFaixaSalarial(usuarioDTO.getFaixaSalarial());

        Usuario updatedUsuario = usuarioService.updateUsuario(id, usuario);
        return updatedUsuario != null ? ResponseEntity.ok(convertToDTO(updatedUsuario)) : ResponseEntity.notFound().build();
    }

    /**
     * Remove um usuário pelo ID.
     *
     * @param id ID do usuário a ser removido
     * @return Um ResponseEntity com status 204 (Sem Conteúdo) após a remoção.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Converte um objeto Usuario em UsuarioDTO.
     *
     * @param usuario Objeto Usuario a ser convertido
     * @return Um objeto UsuarioDTO contendo as informações do usuário.
     */
    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setSenha(usuario.getSenha());
        usuarioDTO.setGenero(usuario.getGenero());
        usuarioDTO.setDataNascimento(usuario.getDataNascimento());
        usuarioDTO.setRamoAtuacao(usuario.getRamoAtuacao());
        usuarioDTO.setFaixaSalarial(usuario.getFaixaSalarial());
        return usuarioDTO;
    }
}
