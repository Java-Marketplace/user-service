package com.jmp.userservice.integration.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import com.jmp.userservice.model.UserStatus;
import com.jmp.userservice.service.keycloak.KeycloakService;
import com.jmp.userservice.support.BaseIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private KeycloakService keycloakService;

    public static final String BASE_USER_URL = "/api/v1/users";

    @Test
    @DisplayName("[CREATE USER] Код 201 : Успешное создание юзера")
    @Transactional
    void shouldCreateUserSuccessfully() {
        CreateUserRequest userRequest = createUserRequest();
        webClient.post()
                .uri(BASE_USER_URL)
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .value(response -> {
                    assertThat(response.getEmail()).isEqualTo(userRequest.getEmail());
                });
    }

    @Test
    @DisplayName("[CREATE USER] Код 400 : Пустое тело запроса")
    @Transactional
    void shouldCreateUserFailWhenEmptyBody() {
        webClient.post()
                .uri(BASE_USER_URL)
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("[CREATE USER] Код 400 : Не валидные параметры при запросе")
    @Transactional
    void shouldCreateUserFailWhenInvalidDataInRequestBody() {
        CreateUserRequest userRequest = new CreateUserRequest(
                "email", "123", "+12345", "Dima"
        );

        webClient.post()
                .uri(BASE_USER_URL)
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("[CREATE USER] Код 409 : Конфликт данных при создание пользователя")
    @Transactional
    void shouldCreateUserFailWhenDuplicateEmailAndPhoneNumber() {
        CreateUserRequest userRequest = createUserRequest();

        webClient.post()
                .uri(BASE_USER_URL)
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated();

        webClient.post()
                .uri(BASE_USER_URL)
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().is4xxClientError();

    }

    @Test
    @DisplayName("[GET USER] Код 200 : Успешное получение юзера по ID")
    @DataSet(value = "db/user/01-user.yml")
    void shouldGetUserSuccessfully() {
        webClient.get()
                .uri(BASE_USER_URL + "/" + "123e4567-e89b-12d3-a456-426614174000")
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    assertThat(response.getEmail()).isEqualTo("shagahod@gmail.com");
                });
    }

    @Test
    @DisplayName("[GET USER] Код 400 : Не правильные параметры запроса")
    void shouldGetUserWhenInvalidId() {
        webClient.get()
                .uri(BASE_USER_URL + "/" + "1")
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("[GET USER] Код 404 : Пользователь не найден")
    void shouldGetUserFailWhenUserNotFound() {
        webClient.get()
                .uri(BASE_USER_URL + "/" + UUID.randomUUID())
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("[DELETE USER] Код 204 : Успешное удаление пользователя")
    @DataSet(value = "db/user/01-user.yml", cleanAfter = true, cleanBefore = true)
    @Transactional
    void shouldDeleteUserSuccessfully() {
        keycloakService.createUser(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "shagahod@gmail.com",
                "+1234567890");

        webClient.delete()
                .uri(BASE_USER_URL + "/" + "123e4567-e89b-12d3-a456-426614174000")
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("[DELETE USER] Код 404 : Пользователь не найден")
    void shouldDeleteUserFailWhenUserNotFound() {
        webClient.delete()
                .uri(BASE_USER_URL + "/" + UUID.randomUUID())
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("[DELETE USER] Код 400 : Не правильные параметры запроса")
    void shouldDeleteUserFailWhenInvalidId() {
        webClient.delete()
                .uri(BASE_USER_URL + "/" + "1")
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("[UPDATE USER] Код 200 : Успешное обновление юзера")
    @Transactional
    @DataSet(value = "db/user/01-user.yml", cleanAfter = true, cleanBefore = true)
    void shouldUpdateUserSuccessfully() {
        webClient.put()
                .uri(BASE_USER_URL + "/" + "123e4567-e89b-12d3-a456-426614174000")
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .bodyValue(updateUserRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    assertThat(response.getEmail()).isEqualTo("updatedDima@gmail.com");
                    assertThat(response.getFirstName()).isEqualTo("Dima");
                });

    }

    @Test
    @DisplayName("[UPDATE USER] Код 400 : Не правильные параметры запроса")
    void shouldUpdateUserFailWhenBadRequest() {
        webClient.put()
                .uri(BASE_USER_URL + "/" + "123e4567-e89b-12d3-a456-426614174000")
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .bodyValue(createUserRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("[UPDATE USER] Код 400 : Не корректный UUID")
    void shouldUpdateUserFailWhenInvalidUUID() {
        webClient.put()
                .uri(BASE_USER_URL + "/" + "1")
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .bodyValue(updateUserRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("[UPDATE USER] Код 404 : Пользователь не найден")
    void shouldUpdateUserFailWhenUserNotFound() {
        webClient.put()
                .uri(BASE_USER_URL + "/" + UUID.randomUUID())
                .header("Authorization", authUtil.getAuthorization("test_admin"))
                .bodyValue(updateUserRequest())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("[UPDATE USER] Код 403 : Вы женщина")
    void shouldUpdateUserFailWhenUserForbidden() {
        webClient.put()
                .uri(BASE_USER_URL + "/" + UUID.randomUUID())
                .header("Authorization", authUtil.getAuthorization("test_support"))
                .bodyValue(updateUserRequest())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("[UPDATE USER] Код 401 : Хуета какая то")
    void shouldUpdateUserFailWhenUserUnauthorized() {
        webClient.put()
                .uri(BASE_USER_URL + "/" + UUID.randomUUID())
                .bodyValue(updateUserRequest())
                .exchange()
                .expectStatus().isUnauthorized();
    }

    private CreateUserRequest createUserRequest() {
        return new CreateUserRequest("dima" + System.currentTimeMillis() + "@gmail.com",
                "1234567990", "+1264567800", "Shagahod");
    }

    private UpdateUserRequest updateUserRequest() {
        return new UpdateUserRequest("updatedDima@gmail.com", "+12345678901", "Dima", "Dimovich", LocalDate.now(),
                "Ia dima shagahod eee", null, UserStatus.ACTIVE
                );
    }

}
