package com.jmp.userservice.service.user;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakService keycloakService;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest dto) {
        validateUniqueUser(null, dto.getEmail(), dto.getPhoneNumber());
        User user = userMapper.toEntity(dto);

        userRepository.save(user);
        keycloakService.createUser(user.getId(), dto.getEmail(), dto.getPassword());

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = findUserById(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest dto) {
        User userForUpdate = findUserById(id);
        validateUniqueUser(id, dto.getEmail(), dto.getPhoneNumber());
        userMapper.updateModel(dto, userForUpdate);
        userRepository.save(userForUpdate);

        return userMapper.toDto(userForUpdate);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        User user = findUserById(id);
        userRepository.delete(user);
        keycloakService.deleteUser(user.getId());
    }

    private void validateUniqueUser(UUID userId, String email, String phoneNumber) {
        userRepository.findByEmail(email)
                .filter(user -> userId == null || !userId.equals(user.getId()))
                .ifPresent(user -> {
                    throw new EmailAlreadyUseException();
                });

        userRepository.findByPhoneNumber(phoneNumber)
                .filter(user -> userId == null || !userId.equals(user.getId()))
                .ifPresent(user -> {
                    throw new PhoneAlreadyUseException();
                });
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundByIdException::new);
    }
}
