package com.controlefacil.controlefacil.exception;

/**
 * Exceção lançada quando uma previsão de gastos já existe.
 */
public class PrevisaoGastosExistenteException extends RuntimeException {

    public PrevisaoGastosExistenteException(String mensagem) {
        super(mensagem);
    }
}
