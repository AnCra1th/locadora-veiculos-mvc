package com.locadoraveiculos.exception;

/**
 * Exceção personalizada para erros relacionados à camada de persistência (DAO).
 */
public class PersistenceException extends RuntimeException {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}