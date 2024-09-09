package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.repository.CategoriaDespesaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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
        when(repository.save(categoria)).thenReturn(categoria);

        CategoriaDespesa result = categoriaDespesaService.save(categoria);
        assertNotNull(result);
        assertEquals(categoria, result);
        verify(repository, times(1)).save(categoria);
    }
}
