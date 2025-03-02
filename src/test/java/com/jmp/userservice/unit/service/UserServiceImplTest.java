package com.jmp.userservice.unit.service;

import com.jmp.userservice.dto.request.UserCreateAccountRequest;
import com.jmp.userservice.dto.response.UserAccountResponse;
import com.jmp.userservice.exception.model.EmailAlreadyUseException;
import com.jmp.userservice.exception.model.PhoneAlreadyUseException;
import com.jmp.userservice.exception.model.UserNotFoundById;
import com.jmp.userservice.mapper.UserMapper;
import com.jmp.userservice.model.User;
import com.jmp.userservice.repository.UserRepository;
import com.jmp.userservice.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldReturnUserAccountResponse_WhenValidRequest() {
        UserCreateAccountRequest requestDto = createUserAccountRequestDto();
        User userModel = createUserModel();
        UserAccountResponse responseDto = createUserAccountResponseDto(userModel);

        when(userMapper.fromUserCreateAccountRequestDtoToUserModel(requestDto)).thenReturn(userModel);
        when(userRepository.save(userModel)).thenReturn(userModel);
        when(userMapper.fromUserModelToUserAccountResponseDto(userModel)).thenReturn(responseDto);

        UserAccountResponse actualResponse = userService.createUser(requestDto);

        assertNotNull(actualResponse);
        assertEquals(responseDto.getId(), actualResponse.getId());
        assertEquals(responseDto.getEmail(), actualResponse.getEmail());
        assertEquals(responseDto.getFirstName(), actualResponse.getFirstName());
        assertEquals(responseDto.getPhoneNumber(), actualResponse.getPhoneNumber());

        verify(userMapper).fromUserCreateAccountRequestDtoToUserModel(requestDto);
        verify(userRepository).save(userModel);
        verify(userMapper).fromUserModelToUserAccountResponseDto(userModel);
    }
    
    @Test
    void createUser_ShouldThrowException_WhenEmailIsAlreadyInUse() {
        UserCreateAccountRequest requestDto = createUserAccountRequestDto();
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);
        assertThrows(EmailAlreadyUseException.class, () -> userService.createUser(requestDto));
        verify(userRepository).existsByEmail(requestDto.getEmail());
    }

    @Test
    void createUser_ShouldThrowException_WhenPhoneNumberIsAlreadyInUse() {
        UserCreateAccountRequest requestDto = createUserAccountRequestDto();
        when(userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())).thenReturn(true);
        assertThrows(PhoneAlreadyUseException.class, () -> userService.createUser(requestDto));
        verify(userRepository).existsByPhoneNumber(requestDto.getPhoneNumber());
    }

    @Test
    void getUser_ShouldReturnUserAccountResponse_WhenValidRequest() {
        User userModel = createUserModel();
        UserAccountResponse responseDto = createUserAccountResponseDto(userModel);

        when(userRepository.findById(userModel.getId())).thenReturn(Optional.of(userModel));
        when(userMapper.fromUserModelToUserAccountResponseDto(userModel)).thenReturn(responseDto);

        UserAccountResponse actualResponse = userService.getUserById(userModel.getId());
        assertNotNull(actualResponse);
        assertEquals(responseDto.getId(), actualResponse.getId());
        assertEquals(responseDto.getEmail(), actualResponse.getEmail());
        assertEquals(responseDto.getFirstName(), actualResponse.getFirstName());
        assertEquals(responseDto.getPhoneNumber(), actualResponse.getPhoneNumber());
    }

    @Test
    void getUser_ShouldThrowException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundById.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId);
    }

    private UserCreateAccountRequest createUserAccountRequestDto(){
        return new UserCreateAccountRequest(
                "test@example.com", "SecurePass123!", "+1234567890", "John");
    }

    private User createUserModel(){
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPhoneNumber("+1234567890");
        user.setFirstName("John");
        return user;
    }

    private UserAccountResponse createUserAccountResponseDto(User user){
        UserAccountResponse response = new UserAccountResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setFirstName(user.getFirstName());
        return response;
    }
}
