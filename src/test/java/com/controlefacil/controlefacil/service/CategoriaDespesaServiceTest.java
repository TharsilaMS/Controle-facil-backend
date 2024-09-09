package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.repository.CategoriaDespesaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaDespesaServiceTest {

    @Mock
    private CategoriaDespesaRepository repository;

    @InjectMocks
    private CategoriaDespesaService categoriaDespesaService;

    @Test
    public void testFindAll() {
        CategoriaDespesa categoria1 = new CategoriaDespesa();
        CategoriaDespesa categoria2 = new CategoriaDespesa();
        when(repository.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        assertEquals(2, categoriaDespesaService.findAll().size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        UUID id = UUID.randomUUID();
        CategoriaDespesa categoria = new CategoriaDespesa();
        when(repository.findById(id)).thenReturn(Optional.of(categoria));

        Optional<CategoriaDespesa> result = categoriaDespesaService.findById(id);
        assertTrue(result.isPresent());
        assertEquals(categoria, result.get());
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void testSave() {
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setNome("Categoria Teste");

        when(repository.findByNome(categoria.getNome())).thenReturn(Optional.empty());
        when(repository.save(categoria)).thenReturn(categoria);

        CategoriaDespesa result = categoriaDespesaService.save(categoria);
        assertNotNull(result);
        assertEquals(categoria, result);
        verify(repository, times(1)).save(categoria);
    }

    @Test
    public void testSaveCategoriaExistente() {
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setNome("Categoria Teste");

        when(repository.findByNome(categoria.getNome())).thenReturn(Optional.of(categoria));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            categoriaDespesaService.save(categoria);
        });

        assertEquals("Categoria com o nome 'Categoria Teste' já existe.", thrown.getMessage());
        verify(repository, never()).save(categoria);
    }

    @Test
    public void testIsCategoriaExistente() {
        String nomeCategoria = "Categoria Teste";
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setNome(nomeCategoria);

        when(repository.findByNome(nomeCategoria)).thenReturn(Optional.of(categoria));

        assertTrue(categoriaDespesaService.isCategoriaExistente(nomeCategoria));
        verify(repository, times(1)).findByNome(nomeCategoria);
    }

    @Test
    public void testIsCategoriaExistenteNaoEncontrada() {
        String nomeCategoria = "Categoria Teste";

        when(repository.findByNome(nomeCategoria)).thenReturn(Optional.empty());

        assertFalse(categoriaDespesaService.isCategoriaExistente(nomeCategoria));
        verify(repository, times(1)).findByNome(nomeCategoria);
    }
}
