package com.jmp.userservice.exception;

public class PhoneAlreadyUseException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Phone already in use";

    public PhoneAlreadyUseException() {
        super(ERROR_MESSAGE);
    }
}
