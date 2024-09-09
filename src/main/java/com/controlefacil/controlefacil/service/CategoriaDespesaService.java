package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.repository.CategoriaDespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaDespesaService {

    @Autowired
    private CategoriaDespesaRepository repository;
    public Optional<CategoriaDespesa> findByNome(String nome) {
        return repository.findByNome(nome);
    }


    public List<CategoriaDespesa> findAll() {
        return repository.findAll();
    }

    public Optional<CategoriaDespesa> findById(UUID id) {
        return repository.findById(id);
    }

    public CategoriaDespesa save(CategoriaDespesa categoriaDespesa) {
        if (isCategoriaExistente(categoriaDespesa.getNome())) {
            throw new IllegalArgumentException("Categoria com o nome '" + categoriaDespesa.getNome() + "' já existe.");
        }
        return repository.save(categoriaDespesa);
    }

    public boolean isCategoriaExistente(String nome) {
        return repository.findByNome(nome).isPresent();
    }


}
