package com.jmp.userservice.controller;


import com.jmp.userservice.dto.request.UserCreateAccountRequest;
import com.jmp.userservice.dto.request.UserUpdateAccountRequest;
import com.jmp.userservice.dto.response.UserAccountResponse;
import com.jmp.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserAccountResponse createUser(@Valid @RequestBody UserCreateAccountRequest dto) {
        return userService.createUser(dto);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserAccountResponse getUserById(@PathVariable("id") UUID id) {
        return userService.getUserById(id);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserAccountResponse updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UserUpdateAccountRequest dto) {
        return userService.updateUser(id, dto);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }
}
