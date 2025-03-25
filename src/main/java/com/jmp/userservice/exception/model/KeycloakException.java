package com.jmp.userservice.exception.model;

public class KeycloakException extends RuntimeException {
    private static final String MESSAGE = "Keycloak exception: ";

    public KeycloakException(String error) {
        super(MESSAGE + error);
    }
}
