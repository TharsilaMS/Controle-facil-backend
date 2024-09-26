package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.repository.CategoriaDespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócios relacionada às categorias de despesa.
 */
@Service
public class CategoriaDespesaService {

    @Autowired
    private CategoriaDespesaRepository repository;

    /**
     * Busca uma categoria de despesa pelo nome.
     *
     * @param nome O nome da categoria.
     * @return Um {@link Optional} contendo a categoria de despesa, se encontrada.
     */
    public Optional<CategoriaDespesa> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    /**
     * Retorna todas as categorias de despesa.
     *
     * @return Uma lista de todas as categorias de despesa.
     */
    public List<CategoriaDespesa> findAll() {
        return repository.findAll();
    }

    /**
     * Busca uma categoria de despesa pelo ID.
     *
     * @param id O ID da categoria.
     * @return Um {@link Optional} contendo a categoria de despesa, se encontrada.
     */
    public Optional<CategoriaDespesa> findById(UUID id) {
        return repository.findById(id);
    }

    /**
     * Salva uma nova categoria de despesa.
     *
     * @param categoriaDespesa A categoria a ser salva.
     * @return A categoria de despesa salva.
     * @throws IllegalArgumentException Se uma categoria com o mesmo nome já existir.
     */
    public CategoriaDespesa save(CategoriaDespesa categoriaDespesa) {
        if (isCategoriaExistente(categoriaDespesa.getNome())) {
            throw new IllegalArgumentException("Categoria com o nome '" + categoriaDespesa.getNome() + "' já existe.");
        }
        return repository.save(categoriaDespesa);
    }

    /**
     * Verifica se uma categoria de despesa com o nome fornecido já existe.
     *
     * @param nome O nome da categoria.
     * @return true se a categoria já existir, false caso contrário.
     */
    public boolean isCategoriaExistente(String nome) {
        return repository.findByNome(nome).isPresent();
    }
}
