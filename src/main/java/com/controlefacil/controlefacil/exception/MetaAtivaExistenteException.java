package com.controlefacil.controlefacil.exception;

/**
 * Exceção lançada quando uma meta ativa já existe.
 */
public class MetaAtivaExistenteException extends RuntimeException {
    public MetaAtivaExistenteException(String message) {
        super(message);
    }
}
