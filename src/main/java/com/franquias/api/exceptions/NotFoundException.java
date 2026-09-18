package com.franquias.api.exceptions;

// 404 - recurso não encontrado
public class NotFoundException extends ApiException {
    public NotFoundException(String mensagem) {
        super(404, mensagem);
    }
}
