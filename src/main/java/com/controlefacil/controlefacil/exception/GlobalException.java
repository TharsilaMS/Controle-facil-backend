package com.controlefacil.controlefacil.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalException {

        @ExceptionHandler(PrevisaoGastosExistenteException.class)
        public ResponseEntity<String> handlePrevisaoGastosExistenteException(PrevisaoGastosExistenteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }

        @ExceptionHandler(RecursoNaoEncontradoException.class)
        public ResponseEntity<String> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    @ExceptionHandler(MetaAtivaExistenteException.class)
    public ResponseEntity<String> handleMetaAtivaExistenteException(MetaAtivaExistenteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    }

