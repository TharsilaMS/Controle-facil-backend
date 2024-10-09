package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.UsuarioDTO;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.UsuarioService;
import com.controlefacil.controlefacil.config.TokenService; // Importar TokenService
import jakarta.validation.Valid;
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

    @Autowired
    private TokenService tokenService;



    @GetMapping("/status")
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioService.getAllUsuarios().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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
    @PostMapping("/create")
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                                    @RequestHeader(value = "Authorization", required = false) String token) {

        String validatedToken = tokenService.validateToken(token);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

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
