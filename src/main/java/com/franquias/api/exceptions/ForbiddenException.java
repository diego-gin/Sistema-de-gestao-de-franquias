package com.franquias.api.exceptions;

/** 403 - autenticado, mas sem permissão (perfil incompatível com a operação). */
public class ForbiddenException extends ApiException {
    public ForbiddenException(String mensagem) {
        super(403, mensagem);
    }
}
