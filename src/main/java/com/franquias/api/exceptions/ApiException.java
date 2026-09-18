package com.franquias.api.exceptions;

// Cada subtipo carrega o código HTTP correspondente
public abstract class ApiException extends RuntimeException {

    private final int statusCode;

    protected ApiException(int statusCode, String mensagem) {
        super(mensagem);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
