package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.ResourceNotFoundException;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Despesa> getAllDespesas() {
        return despesaRepository.findAll();
    }

    public Optional<Despesa> getDespesaById(Long id) {
        return despesaRepository.findById(id);
    }

    public Despesa saveDespesa(Despesa despesa) {
        Usuario usuario = usuarioRepository.findById(despesa.getUsuario().getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        despesa.setUsuario(usuario);

        return despesaRepository.save(despesa);
    }

    public Despesa updateDespesa(Long id, Despesa despesaDetails) {
        Despesa despesa = getDespesaById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada com o id " + id));

        Usuario usuario = usuarioRepository.findById(despesaDetails.getUsuario().getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        despesa.setDescricao(despesaDetails.getDescricao());
        despesa.setValor(despesaDetails.getValor());
        despesa.setData(despesaDetails.getData());
        despesa.setUsuario(usuario);
        despesa.setCategoriaDespesa(despesaDetails.getCategoriaDespesa());
        despesa.setTipo(despesaDetails.getTipo());

        return despesaRepository.save(despesa);
    }

    public void deleteDespesa(Long id) {
        Despesa despesa = getDespesaById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada com o id " + id));
        despesaRepository.delete(despesa);
    }

    public List<Despesa> getDespesasByUsuarioId(Long idUsuario) {
        return despesaRepository.findByUsuario_IdUsuario(idUsuario);
    }
}
