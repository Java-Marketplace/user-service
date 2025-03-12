package com.jmp.userservice.controller.user;

import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@RequestMapping("/api/v1/users")
@Tag(name = "User Controller")
public interface UserController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Новый пользователя успешно создан"),
            @ApiResponse(responseCode = "400", description = "Не правильные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка ответа от сервера")
    })
    @Operation(summary = "Создание нового пользователя", description = "Создает нового пользователя в базе юзеров")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    UserResponse createUser(@Valid @RequestBody CreateUserRequest dto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получение пользователя по UUID"),
            @ApiResponse(responseCode = "400", description = "Указан не верный формат UUID"),
            @ApiResponse(responseCode = "404", description = "Пользователя с таким UUID не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка ответа от сервера")
    })
    @Operation(summary = "Получение пользователя", description = "Получение пользователя по UUID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserResponse getUserById(@PathVariable UUID id);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно обновление пользователя"),
            @ApiResponse(responseCode = "400", description = "Не правильные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка ответа от сервера")
    })
    @Operation(summary = "Обновление пользователя", description = "Обновление пользователя по его UUID")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest dto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно удаление пользователя"),
            @ApiResponse(responseCode = "400", description = "Не правильные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка ответа от сервера")
    })
    @Operation(summary = "Удаление пользователя", description = "Удаляет пользователя из базы данных")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable UUID id);
}
