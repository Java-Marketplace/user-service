package com.jmp.userservice.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmp.userservice.constant.UserStatus;
import com.jmp.userservice.controller.UserControllerImpl;
import com.jmp.userservice.dto.request.UserCreateAccountRequest;
import com.jmp.userservice.dto.response.UserAccountResponse;
import com.jmp.userservice.exception.model.EmailAlreadyUseException;
import com.jmp.userservice.exception.model.PhoneAlreadyUseException;
import com.jmp.userservice.exception.model.UserNotFoundById;
import com.jmp.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerImpl.class)
@ExtendWith(MockitoExtension.class)
class UserControllerImplTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturn201_WhenValidRequest() throws Exception {
        UserCreateAccountRequest requestDto = new UserCreateAccountRequest(
                "test@example.com",
                "SecurePass123!",
                "+1234567890",
                "John"
        );
        UserAccountResponse responseDto = createUserResponseDto();

        doReturn(responseDto).when(userService).createUser(any(UserCreateAccountRequest.class));

        mvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
        verify(userService, times(1)).createUser(any(UserCreateAccountRequest.class));
    }

    @Test
    void createUser_ShouldReturn400_WhenInvalidFieldInDtoRequest() throws Exception {
        UserCreateAccountRequest invalidRequestDto = new UserCreateAccountRequest(
                "", "123", "+1234567890", "John"
        );
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_ShouldReturn400_WhenEmptyFieldInDtoRequest() throws Exception {
        String dtoJson = "{}";
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_ShouldReturn409_WhenEmailAlreadyExists() throws Exception {
        UserCreateAccountRequest request = createRequestDto();

        doThrow(new EmailAlreadyUseException())
                .when(userService).createUser(any(UserCreateAccountRequest.class));

        mvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already in use"));
    }

    @Test
    void createUser_ShouldReturn409_WhenPhoneNumberAlreadyExists() throws Exception {
        UserCreateAccountRequest request = createRequestDto();
        doThrow(new PhoneAlreadyUseException())
                .when(userService).createUser(any(UserCreateAccountRequest.class));
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Phone already in use"));
    }

    @Test
    void getUserById_ShouldReturn200_WhenValidRequest() throws Exception {
        UserAccountResponse responseDto = createUserResponseDto();
        doReturn(responseDto).when(userService).getUserById(responseDto.getId());

        mvc.perform(get("/api/v1/users/" + responseDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

    }

    @Test
    void getUserById_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        doThrow(new UserNotFoundById()).when(userService).getUserById(any());
        mvc.perform(get("/api/v1/users/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_ShouldReturn400_WhenInvalidUserId() throws Exception {
        mvc.perform(get("/api/v1/users/"))
                .andExpect(status().isNotFound());
    }

    private UserAccountResponse createUserResponseDto() {
        UserAccountResponse responseDto = new UserAccountResponse();
        responseDto.setId(UUID.randomUUID());
        responseDto.setEmail("test@example.com");
        responseDto.setPhoneNumber("+1234567890");
        responseDto.setFirstName("John");
        responseDto.setLastName("Doe");
        responseDto.setBirthDate("1990-01-01");
        responseDto.setAboutMe("Software Engineer");
        responseDto.setLinks(Map.of());
        responseDto.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        responseDto.setStatus(UserStatus.ACTIVE);
        return responseDto;
    }

    private UserCreateAccountRequest createRequestDto() {
        return new UserCreateAccountRequest(
                "existing@example.com", "SecurePass123!", "+1234567890", "John");
    }
}
