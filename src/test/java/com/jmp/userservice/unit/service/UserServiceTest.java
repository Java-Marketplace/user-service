package com.jmp.userservice.unit.service;

import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import com.jmp.userservice.exception.EmailAlreadyUseException;
import com.jmp.userservice.exception.PhoneAlreadyUseException;
import com.jmp.userservice.exception.UserNotFoundByIdException;
import com.jmp.userservice.mapper.UserMapper;
import com.jmp.userservice.model.User;
import com.jmp.userservice.repository.UserRepository;
import com.jmp.userservice.service.keycloak.KeycloakService;
import com.jmp.userservice.service.user.UserServiceImpl;
import com.jmp.userservice.support.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @Transactional
    void createUserSuccess() {
        CreateUserRequest userRequest = createUserRequest();

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(userRequest.getPhoneNumber())).thenReturn(Optional.empty());


        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());

        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(keycloakService).createUser(any(), any(), any());
        when(userMapper.toDto(user)).thenReturn(UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build());

        UserResponse userResponse = userService.createUser(userRequest);

        assertThat(userResponse.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(userRequest.getPhoneNumber());

        verify(userRepository).save(user);
        verify(keycloakService).createUser(any(), any(), any());
        verify(userMapper).toDto(user);
        verify(userMapper).toEntity(userRequest);
    }

    @Test
    @Transactional
    void failCreateUser_WhenPhoneAlreadyUsed() {
        CreateUserRequest userRequest = createUserRequest();

        User exctingUser = new User();
        exctingUser.setPhoneNumber(userRequest.getPhoneNumber());

        when(userRepository.findByPhoneNumber(userRequest.getPhoneNumber())).thenReturn(Optional.of(exctingUser));

        assertThrows(PhoneAlreadyUseException.class, () -> userService.createUser(userRequest));

        verify(userRepository, never()).save(any());
        verify(keycloakService, never()).createUser(any(), any(), any());
    }


    @Test
    @Transactional
    void failCreateUser_WhenEmailAlreadyUsed() {
        CreateUserRequest userRequest = createUserRequest();

        User exctingUser = new User();
        exctingUser.setEmail(userRequest.getEmail());

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(exctingUser));

        assertThrows(EmailAlreadyUseException.class, () -> userService.createUser(userRequest));

        verify(userRepository, never()).save(any());
        verify(keycloakService, never()).createUser(any(), any(), any());
    }

    @Test
    @Transactional
    void getUserByIdSuccess() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@email.com");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build());
        UserResponse userResponse = userService.getUserById(user.getId());
        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());

        verify(userRepository).findById(user.getId());
        verify(userMapper).toDto(user);
    }

    @Test
    @Transactional
    void failGetUserById_WhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundByIdException.class, () -> userService.getUserById(id));
    }

    @Test
    @Transactional
    void shouldUpdateUserSuccessfully() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setPhoneNumber("+123456789");
        existingUser.setEmail("old_email@example.com");
        existingUser.setFirstName("Old Name");

        UpdateUserRequest updateRequest = UpdateUserRequest.builder()
                .email("new_email@example.com")
                .phoneNumber("+987654321")
                .firstName("New Name")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doAnswer(invocation -> {
            UpdateUserRequest request = invocation.getArgument(0);
            User user = invocation.getArgument(1);

            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setFirstName(request.getFirstName());

            return null;
        }).when(userMapper).updateModel(updateRequest, existingUser);

        when(userMapper.toDto(existingUser)).thenReturn(UserResponse.builder()
                .id(userId)
                .email(updateRequest.getEmail())
                .phoneNumber(updateRequest.getPhoneNumber())
                .firstName(updateRequest.getFirstName())
                .build());

        UserResponse response = userService.updateUser(userId, updateRequest);

        assertThat(response.getEmail()).isEqualTo(updateRequest.getEmail());
        assertThat(response.getPhoneNumber()).isEqualTo(updateRequest.getPhoneNumber());
        assertThat(response.getFirstName()).isEqualTo(updateRequest.getFirstName());

        verify(userRepository).save(any());
    }

    @Test
    @Transactional
    void failUpdateUser_WhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Executable executable = () -> userService.updateUser(userId, new UpdateUserRequest());

        assertThrows(UserNotFoundByIdException.class, executable);
    }

    private CreateUserRequest createUserRequest() {
        return new CreateUserRequest("dima" + System.currentTimeMillis() + "@gmail.com",
                "1234567990", "+1264567800", "Shagahod");
    }

}
