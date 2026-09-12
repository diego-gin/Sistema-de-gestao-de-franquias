package com.franquias.api.exceptions;

/** 409 - conflito com o estado atual dos dados (ex: e-mail ou CNPJ duplicado). */
public class ConflictException extends ApiException {
    public ConflictException(String mensagem) {
        super(409, mensagem);
    }
}
