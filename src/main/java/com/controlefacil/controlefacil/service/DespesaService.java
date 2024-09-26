package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.RecursoNaoEncontradoException;
import com.controlefacil.controlefacil.model.CategoriaDespesa;
import com.controlefacil.controlefacil.model.Despesa;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.repository.DespesaRepository;
import com.controlefacil.controlefacil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço que gerencia as operações relacionadas a despesas.
 */
@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaDespesaService categoriaDespesaService;

    /**
     * Recupera todas as despesas.
     *
     * @return Uma lista de todas as despesas.
     */
    public List<Despesa> getAllDespesas() {
        return despesaRepository.findAll();
    }

    /**
     * Busca uma despesa pelo ID.
     *
     * @param id O ID da despesa.
     * @return Um {@link Optional} contendo a despesa, se encontrada.
     */
    public Optional<Despesa> getDespesaById(UUID id) {
        return despesaRepository.findById(id);
    }

    /**
     * Salva uma nova despesa.
     *
     * @param despesa A despesa a ser salva.
     * @return A despesa salva.
     * @throws IllegalArgumentException Se a data da despesa for nula.
     * @throws RecursoNaoEncontradoException Se o usuário não for encontrado.
     */
    public Despesa saveDespesa(Despesa despesa) {
        if (despesa.getData() == null) {
            throw new IllegalArgumentException("A data da despesa não pode ser nula.");
        }

        Usuario usuario = usuarioRepository.findById(despesa.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        CategoriaDespesa categoria = categoriaDespesaService.findByNome(despesa.getCategoriaDespesa().getNome())
                .orElseGet(() -> {
                    CategoriaDespesa novaCategoria = new CategoriaDespesa();
                    novaCategoria.setNome(despesa.getCategoriaDespesa().getNome());
                    return categoriaDespesaService.save(novaCategoria);
                });

        despesa.setUsuario(usuario);
        despesa.setCategoriaDespesa(categoria);
        return despesaRepository.save(despesa);
    }

    /**
     * Atualiza uma despesa existente.
     *
     * @param id O ID da despesa a ser atualizada.
     * @param despesaDetails Os novos detalhes da despesa.
     * @return A despesa atualizada.
     * @throws RecursoNaoEncontradoException Se a despesa ou o usuário não forem encontrados.
     */
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

    /**
     * Remove uma despesa pelo ID.
     *
     * @param id O ID da despesa a ser removida.
     * @throws RecursoNaoEncontradoException Se a despesa não for encontrada.
     */
    public void deleteDespesa(UUID id) {
        Despesa despesa = getDespesaById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa não encontrada com o id " + id));
        despesaRepository.delete(despesa);
    }

    /**
     * Recupera as despesas de um usuário específico.
     *
     * @param idUsuario O ID do usuário.
     * @return Uma lista de despesas associadas ao usuário.
     */
    public List<Despesa> getDespesasByUsuarioId(UUID idUsuario) {
        return despesaRepository.findByUsuario_IdUsuario(idUsuario);
    }
}
