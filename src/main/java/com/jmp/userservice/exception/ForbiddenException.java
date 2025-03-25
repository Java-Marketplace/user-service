package com.jmp.userservice.exception;

public class ForbiddenException extends RuntimeException {
    private static final String MESSAGE = "You don't have permission to access this resource";

    public ForbiddenException() {
        super(MESSAGE);
    }
}
