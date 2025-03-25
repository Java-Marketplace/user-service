package com.jmp.userservice.exception;

public class UserNotFoundByIdException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User not found by id";

    public UserNotFoundByIdException() {
        super(ERROR_MESSAGE);
    }
}
