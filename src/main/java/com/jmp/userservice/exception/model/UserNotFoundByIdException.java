package com.jmp.userservice.exception.model;

public class UserNotFoundByIdException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User not found by id";
    public UserNotFoundByIdException() {
        super(ERROR_MESSAGE);
    }
}
