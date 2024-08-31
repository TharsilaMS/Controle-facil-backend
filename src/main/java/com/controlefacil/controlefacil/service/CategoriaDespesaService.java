package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.repository.CategoriaDespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaDespesaService {

    @Autowired
    private CategoriaDespesaRepository repository;

    public List<CategoriaDespesa> findAll() {
        return repository.findAll();
    }

    public Optional<CategoriaDespesa> findById(Long id) {
        return repository.findById(id);
    }

    public CategoriaDespesa save(CategoriaDespesa categoriaDespesa) {
        return repository.save(categoriaDespesa);
    }


}
