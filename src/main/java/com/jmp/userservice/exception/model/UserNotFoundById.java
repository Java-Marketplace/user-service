package com.jmp.userservice.exception.model;

public class UserNotFoundById extends RuntimeException {
    private static final String ERROR_MESSAGE = "User not found by id";
    public UserNotFoundById() {
        super(ERROR_MESSAGE);
    }
}
