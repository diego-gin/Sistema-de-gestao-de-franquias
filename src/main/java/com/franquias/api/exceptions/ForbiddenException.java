package com.franquias.api.exceptions;

// Autenticado, mas sem permissão
public class ForbiddenException extends ApiException {
    public ForbiddenException(String mensagem) {
        super(403, mensagem);
    }
}
