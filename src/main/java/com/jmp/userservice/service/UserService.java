package com.jmp.userservice.service;

import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest user);

    UserResponse getUserById(UUID id);

    UserResponse updateUser(UUID id, UpdateUserRequest user);

    void deleteUser(UUID id);
}
