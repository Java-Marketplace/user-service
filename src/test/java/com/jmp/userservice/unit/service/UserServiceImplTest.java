package com.jmp.userservice.unit.service;

import com.jmp.userservice.dto.request.UserCreateAccountRequest;
import com.jmp.userservice.dto.request.UserUpdateAccountRequest;
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
        User existingUser = createUserModel();
        existingUser.setEmail(requestDto.getEmail());

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyUseException.class, () -> userService.createUser(requestDto));

        verify(userRepository).findByEmail(requestDto.getEmail());
    }

    @Test
    void createUser_ShouldThrowException_WhenPhoneNumberIsAlreadyInUse() {
        UserCreateAccountRequest requestDto = createUserAccountRequestDto();
        User existingUser = createUserModel();
        existingUser.setPhoneNumber(requestDto.getPhoneNumber());

        when(userRepository.findByPhoneNumber(requestDto.getPhoneNumber())).thenReturn(Optional.of(existingUser));

        assertThrows(PhoneAlreadyUseException.class, () -> userService.createUser(requestDto));

        verify(userRepository).findByPhoneNumber(requestDto.getPhoneNumber());
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

    @Test
    void UpdateUser_ShouldReturnUserAccountResponse_WhenValidRequest() {
        UUID userId = UUID.randomUUID();
        UserUpdateAccountRequest dto = createUserUpdateRequestDto();

        User existingUser = createUserModel();
        existingUser.setId(userId);

        UserAccountResponse expectedResponse = createUserAccountResponseDto(existingUser);
        expectedResponse.setEmail("updated@example.com");
        expectedResponse.setPhoneNumber("+123456789");
        expectedResponse.setFirstName("UpdatedFirstName");
        expectedResponse.setLastName("UpdatedLastName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.fromUserModelToUserAccountResponseDto(any(User.class))).thenReturn(expectedResponse);

        UserAccountResponse actualResponse = userService.updateUser(userId, dto);

        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
        assertEquals(expectedResponse.getPhoneNumber(), actualResponse.getPhoneNumber());
        assertEquals(expectedResponse.getFirstName(), actualResponse.getFirstName());
        assertEquals(expectedResponse.getLastName(), actualResponse.getLastName());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).fromUserModelToUserAccountResponseDto(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundById.class, () -> userService.getUserById(userId));
    }

    @Test
    void updateUser_ShouldThrowException_WhenPhoneNumberIsAlreadyInUse() {
        UUID userId = UUID.randomUUID();
        UserUpdateAccountRequest dto = createUserUpdateRequestDto();

        User existingUser = createUserModel();
        existingUser.setId(userId);
        existingUser.setEmail("existing@example.com");
        existingUser.setPhoneNumber("+987654321");

        User anotherUserWithSamePhone = createUserModel();
        anotherUserWithSamePhone.setId(UUID.randomUUID());
        anotherUserWithSamePhone.setPhoneNumber(dto.getPhoneNumber());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByPhoneNumber(dto.getPhoneNumber())).thenReturn(Optional.of(anotherUserWithSamePhone));

        assertThrows(PhoneAlreadyUseException.class, () -> userService.updateUser(userId, dto));

        verify(userRepository).findById(userId);
        verify(userRepository).findByPhoneNumber(dto.getPhoneNumber());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnException_WhenEmailIsAlreadyInUse() {
        UUID userId = UUID.randomUUID();
        UserUpdateAccountRequest dto = createUserUpdateRequestDto();

        User existingUser = createUserModel();
        existingUser.setId(userId);
        existingUser.setEmail("existing@example.com");
        existingUser.setPhoneNumber("+987654321");

        User anotherUserWithSameEmail = createUserModel();
        anotherUserWithSameEmail.setId(UUID.randomUUID());
        anotherUserWithSameEmail.setPhoneNumber(dto.getPhoneNumber());
        anotherUserWithSameEmail.setEmail(dto.getEmail());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(anotherUserWithSameEmail));

        assertThrows(EmailAlreadyUseException.class, () -> userService.updateUser(userId, dto));

        verify(userRepository).findById(userId);
        verify(userRepository).findByEmail(dto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldNotThrowException_WhenEmailBelongToUser() {
        UUID userId = UUID.randomUUID();
        UserUpdateAccountRequest requestDto = createUserUpdateRequestDto();
        User existingUser = createUserModel();
        existingUser.setId(userId);
        existingUser.setEmail(requestDto.getEmail());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userService.updateUser(userId, requestDto));
        verify(userRepository).findById(userId);
        verify(userRepository).findByEmail(requestDto.getEmail());
    }

    @Test
    void updateUser_ShouldNotThrowException_WhenPhoneNumberBelongToUser() {
        UUID userId = UUID.randomUUID();
        UserUpdateAccountRequest dto = createUserUpdateRequestDto();
        User existingUser = createUserModel();
        existingUser.setId(userId);
        existingUser.setPhoneNumber(dto.getPhoneNumber());
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByPhoneNumber(dto.getPhoneNumber())).thenReturn(Optional.of(existingUser));
        assertDoesNotThrow(() -> userService.updateUser(userId, dto));
        verify(userRepository).findById(userId);
        verify(userRepository).findByPhoneNumber(dto.getPhoneNumber());
    }

    @Test
    void deleteUser_ShouldReturnUserAccountResponse_WhenValidRequest() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(createUserModel()));
        userService.deleteUser(userId);
        verify(userRepository).findById(userId);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundById.class, () -> userService.deleteUser(userId));
    }

    private UserUpdateAccountRequest createUserUpdateRequestDto(){
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setEmail("updated@example.com");
        dto.setPhoneNumber("+123456789");
        dto.setFirstName("UpdatedFirstName");
        dto.setLastName("UpdatedLastName");
        return dto;
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
