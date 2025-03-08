package com.jmp.userservice.service;

import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import com.jmp.userservice.exception.model.EmailAlreadyUseException;
import com.jmp.userservice.exception.model.PhoneAlreadyUseException;
import com.jmp.userservice.exception.model.UserNotFoundByIdException;
import com.jmp.userservice.mapper.UserMapper;
import com.jmp.userservice.model.User;
import com.jmp.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponse createUser(CreateUserRequest dto) {
        validateUniqueUser(null, dto.getEmail(), dto.getPhoneNumber());
        User user = userMapper.toEntity(dto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserById(UUID id) {
        User user = findUserById(id);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest dto) {
        User userForUpdate = findUserById(id);
        validateUniqueUser(id, dto.getEmail(), dto.getPhoneNumber());
        userMapper.updateModel(dto, userForUpdate);
        userRepository.save(userForUpdate);
        return userMapper.toDto(userForUpdate);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = findUserById(id);
        userRepository.delete(user);
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
