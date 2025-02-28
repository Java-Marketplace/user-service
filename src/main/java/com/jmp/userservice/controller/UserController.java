package com.jmp.userservice.controller;

import com.jmp.userservice.dto.request.UserCreateAccountRequest;
import com.jmp.userservice.dto.request.UserUpdateAccountRequest;
import com.jmp.userservice.dto.response.UserAccountResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface UserController {

    @PostMapping
    UserAccountResponse createUser(@Valid @RequestBody UserCreateAccountRequest dto);

    @GetMapping("/{id}")
    UserAccountResponse getUserById(@PathVariable("id") UUID id);

    @PutMapping("/{id}")
    UserAccountResponse updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UserUpdateAccountRequest dto);

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable("id") UUID id);
}
