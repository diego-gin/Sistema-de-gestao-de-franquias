package com.franquias.api.exceptions;

// 401 - Não autenticado - Token inválido
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String mensagem) {
        super(401, mensagem);
    }
}
