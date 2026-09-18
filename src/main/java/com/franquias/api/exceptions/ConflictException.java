package com.franquias.api.exceptions;

// Conflito tipo dados duplicados
public class ConflictException extends ApiException {
    public ConflictException(String mensagem) {
        super(409, mensagem);
    }
}
