package com.jmp.userservice.service;

import com.jmp.userservice.dto.request.UserCreateRequest;
import com.jmp.userservice.dto.request.UserUpdateRequest;
import com.jmp.userservice.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserCreateRequest user);
    UserResponse getUserById(UUID id);
    UserResponse updateUser(UUID id, UserUpdateRequest user);
    void deleteUser(UUID id);
}
