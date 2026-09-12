package com.franquias.api.exceptions;

/**
 * Classe base para exceções de negócio da API.
 * Cada subtipo carrega o código HTTP que deve ser retornado ao cliente.
 */
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
