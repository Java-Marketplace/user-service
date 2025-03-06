package com.jmp.userservice.controller;

import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface UserController {

    @PostMapping
    UserResponse createUser(@Valid @RequestBody CreateUserRequest dto);

    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable UUID id);

    @PutMapping("/{id}")
    UserResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest dto);

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable UUID id);
}
