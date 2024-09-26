package com.controlefacil.controlefacil.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Classe de tratamento global de exceções.
 */
@ControllerAdvice
public class GlobalException {

    /**
     * Trata exceções de previsão de gastos existentes.
     *
     * @param ex A exceção lançada.
     * @return A resposta com status de conflito e a mensagem da exceção.
     */
    @ExceptionHandler(PrevisaoGastosExistenteException.class)
    public ResponseEntity<String> handlePrevisaoGastosExistenteException(PrevisaoGastosExistenteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Trata exceções de recurso não encontrado.
     *
     * @param ex A exceção lançada.
     * @return A resposta com status de não encontrado e a mensagem da exceção.
     */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<String> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Trata exceções de meta ativa existente.
     *
     * @param ex A exceção lançada.
     * @return A resposta com status de solicitação inválida e a mensagem da exceção.
     */
    @ExceptionHandler(MetaAtivaExistenteException.class)
    public ResponseEntity<String> handleMetaAtivaExistenteException(MetaAtivaExistenteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
