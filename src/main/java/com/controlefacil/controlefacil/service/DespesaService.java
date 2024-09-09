package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import com.controlefacil.controlefacil.service.CategoriaDespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaDespesaService categoriaDespesaService;

    public List<Despesa> getAllDespesas() {
        return despesaRepository.findAll();
    }

    public Optional<Despesa> getDespesaById(UUID id) {
        return despesaRepository.findById(id);
    }

    public Despesa saveDespesa(Despesa despesa) {
        // Verificar se o usuário existe
        Usuario usuario = usuarioRepository.findById(despesa.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        // Verificar se a categoria já existe, senão criar uma nova
        CategoriaDespesa categoria = categoriaDespesaService.findByNome(despesa.getCategoriaDespesa().getNome())
                .orElseGet(() -> {
                    // Se a categoria não existir, crie uma nova
                    CategoriaDespesa novaCategoria = new CategoriaDespesa();
                    novaCategoria.setNome(despesa.getCategoriaDespesa().getNome());
                    return categoriaDespesaService.save(novaCategoria); // Aqui garantimos que a categoria seja salva
                });

        // Associar o usuário e a categoria à despesa
        despesa.setUsuario(usuario);
        despesa.setCategoriaDespesa(categoria);

        // Salvar a despesa
        return despesaRepository.save(despesa);
    }


    public Despesa updateDespesa(UUID id, Despesa despesaDetails) {
        Despesa despesa = getDespesaById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa não encontrada com o id " + id));

        Usuario usuario = usuarioRepository.findById(despesaDetails.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        CategoriaDespesa categoria = categoriaDespesaService.findByNome(despesaDetails.getCategoriaDespesa().getNome())
                .orElseGet(() -> {
                    CategoriaDespesa novaCategoria = new CategoriaDespesa();
                    novaCategoria.setNome(despesaDetails.getCategoriaDespesa().getNome());
                    return categoriaDespesaService.save(novaCategoria);
                });

        despesa.setDescricao(despesaDetails.getDescricao());
        despesa.setValor(despesaDetails.getValor());
        despesa.setData(despesaDetails.getData());
        despesa.setUsuario(usuario);
        despesa.setCategoriaDespesa(categoria);
        despesa.setTipo(despesaDetails.getTipo());

        return despesaRepository.save(despesa);
    }

    public void deleteDespesa(UUID id) {
        Despesa despesa = getDespesaById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa não encontrada com o id " + id));
        despesaRepository.delete(despesa);
    }

    public List<Despesa> getDespesasByUsuarioId(UUID idUsuario) {
        return despesaRepository.findByUsuario_IdUsuario(idUsuario);
    }
}
