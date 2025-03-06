package com.jmp.userservice.controller;

import com.jmp.userservice.dto.request.UserCreateRequest;
import com.jmp.userservice.dto.request.UserUpdateRequest;
import com.jmp.userservice.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface UserController {

    @PostMapping
    UserResponse createUser(@Valid @RequestBody UserCreateRequest dto);

    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable UUID id);

    @PutMapping("/{id}")
    UserResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest dto);

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable UUID id);
}
