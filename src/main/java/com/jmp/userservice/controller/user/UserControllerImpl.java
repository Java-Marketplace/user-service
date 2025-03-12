package com.jmp.userservice.controller.user;


import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import com.jmp.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override

    public UserResponse createUser(@Valid @RequestBody CreateUserRequest dto) {
        return userService.createUser(dto);
    }

    @Override

    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @Override
    public UserResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest dto) {
        return userService.updateUser(id, dto);
    }

    @Override
    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }
}
