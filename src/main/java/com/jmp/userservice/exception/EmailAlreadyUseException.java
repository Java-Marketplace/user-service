package com.jmp.userservice.exception;

public class EmailAlreadyUseException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Email already in use";

    public EmailAlreadyUseException() {
        super(ERROR_MESSAGE);
    }
}
