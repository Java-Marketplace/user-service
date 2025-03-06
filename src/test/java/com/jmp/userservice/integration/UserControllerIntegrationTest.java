package com.jmp.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import com.jmp.userservice.model.User;
import com.jmp.userservice.model.UserStatus;
import com.jmp.userservice.repository.UserRepository;
import com.jmp.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        CreateUserRequest dto = new CreateUserRequest("dima@it-top.academy", "1234567890",
                "+123456789", "John");
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("dima@it-top.academy"))
                .andExpect(jsonPath("$.phoneNumber").value("+123456789"));

        Optional<User> user = userRepository.findByEmail("dima@it-top.academy");
        assertTrue(user.isPresent());
        assertEquals("+123456789", user.get().getPhoneNumber());
        assertEquals("dima@it-top.academy", user.get().getEmail());

        UserResponse createUser = userService.getUserById(user.get().getId());
        assertNotNull(createUser);
        assertEquals("dima@it-top.academy", createUser.getEmail());
        assertEquals("+123456789", createUser.getPhoneNumber());
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        CreateUserRequest dto = new CreateUserRequest("dima@it-top.academy", "1234567890",
                "+1234567890", "John");
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Optional<User> userOptional = userRepository.findByEmail("dima@it-top.academy");
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();

        UpdateUserRequest updateDto = new UpdateUserRequest();
        updateDto.setPhoneNumber("+987654321");
        updateDto.setEmail("newdima@it-top.academy");
        updateDto.setLastName("newdima");
        updateDto.setFirstName("newdima");
        updateDto.setBirthday(LocalDate.parse("2022-12-22"));
        updateDto.setStatus(UserStatus.ACTIVE);
        mockMvc.perform(put("/api/v1/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newdima@it-top.academy"))
                .andExpect(jsonPath("$.phoneNumber").value("+987654321"));

        Optional<User> updatedUser = userRepository.findByEmail("newdima@it-top.academy");
        assertTrue(updatedUser.isPresent());
        assertEquals("+987654321", updatedUser.get().getPhoneNumber());
        assertEquals("newdima@it-top.academy", updatedUser.get().getEmail());

        UserResponse userResponse = userService.getUserById(updatedUser.get().getId());
        assertNotNull(userResponse);
        assertEquals("newdima@it-top.academy", userResponse.getEmail());
        assertEquals("+987654321", userResponse.getPhoneNumber());
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        CreateUserRequest dto = new CreateUserRequest("dima@it-top.academy", "1234567890",
                "+123456789", "John");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Optional<User> userOptional = userRepository.findByEmail("dima@it-top.academy");
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();

        mockMvc.perform(delete("/api/v1/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        Optional<User> deletedUser = userRepository.findByEmail("dima@it-top.academy");
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void shouldGetUserByIdSuccessfully() throws Exception {
        CreateUserRequest dto = new CreateUserRequest("dima@it-top.academy", "1234567890",
                "+123456789", "John");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Optional<User> userOptional = userRepository.findByEmail("dima@it-top.academy");
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        mockMvc.perform(get("/api/v1/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("dima@it-top.academy"))
                .andExpect(jsonPath("$.phoneNumber").value("+123456789"));
    }

}
