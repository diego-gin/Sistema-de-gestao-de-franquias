package com.franquias.api.exceptions;

// 400 - Dados inválidos ou requisição malformada
public class BadRequestException extends ApiException {
    public BadRequestException(String mensagem) {
        super(400, mensagem);
    }
}
