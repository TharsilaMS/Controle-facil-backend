package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.CategoriaDespesaRepository;
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

    @Autowired
    private CategoriaDespesaRepository categoriaDespesaRepository;

    public List<Despesa> getAllDespesas() {
        return despesaRepository.findAll();
    }

    public Optional<Despesa> getDespesaById(Long id) {
        return despesaRepository.findById(id);
    }

    public Despesa saveDespesa(Despesa despesa) {
        Usuario usuario = usuarioRepository.findById(despesa.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CategoriaDespesa categoriaDespesa = categoriaDespesaRepository.findById(despesa.getCategoriaDespesa().getId())
                .orElseThrow(() -> new RuntimeException("Categoria de despesa não encontrada"));

        despesa.setUsuario(usuario);
        despesa.setCategoriaDespesa(categoriaDespesa);

        return despesaRepository.save(despesa);
    }

    public void deleteDespesa(Long id) {
        despesaRepository.deleteById(id);
    }
}
