package com.jmp.userservice.service;

import com.jmp.userservice.dto.request.UserCreateAccountRequest;
import com.jmp.userservice.dto.request.UserUpdateAccountRequest;
import com.jmp.userservice.dto.response.UserAccountResponse;

import java.util.UUID;

public interface UserService {
    UserAccountResponse createUser(UserCreateAccountRequest user);
    UserAccountResponse getUserById(UUID id);
    UserAccountResponse updateUser(UUID id, UserUpdateAccountRequest user);
    void deleteUser(UUID id);
}
