package com.jmp.userservice.controller.user;


import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import com.jmp.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UserResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest dto) {
        return userService.updateUser(id, dto);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }
}
