package com.franquias.api.exceptions;

/** 401 - não autenticado (token ausente, inválido ou expirado; credenciais erradas). */
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String mensagem) {
        super(401, mensagem);
    }
}
