package com.jmp.userservice.mapper;

import com.jmp.userservice.constant.UserStatus;
import com.jmp.userservice.dto.request.UserCreateAccountRequest;
import com.jmp.userservice.dto.request.UserUpdateAccountRequest;
import com.jmp.userservice.dto.response.UserAccountResponse;
import com.jmp.userservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void fromUserCreateAccountRequestDtoToUserModel_ShouldMapCorrectly() {
        UserCreateAccountRequest dto = new UserCreateAccountRequest("test@example.com", "12345678",
                "12345678", "John");
        User user = userMapper.fromUserCreateAccountRequestDtoToUserModel(dto);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("John", user.getFirstName());
    }

    @Test
    void fromUserCreateAccountRequestDtoToUserModel_WithNullDto_ShouldReturnNull() {
        User user = userMapper.fromUserCreateAccountRequestDtoToUserModel(null);
        assertNull(user);
    }

    @Test
    void fromUserCreateAccountRequestDtoToUserModel_ShouldHandleNullValues(){
        UserCreateAccountRequest dto = new UserCreateAccountRequest(null, null, null, null);
        User user = userMapper.fromUserCreateAccountRequestDtoToUserModel(dto);
        assertNotNull(user);
        assertNull(user.getEmail());
        assertNull(user.getPhoneNumber());
        assertNull(user.getFirstName());
    }

    @Test
    void fromUserCreateAccountRequestDtoToUserModel_ShouldHandleEmptyStrings(){
        UserCreateAccountRequest dto = new UserCreateAccountRequest("", "", "", "");
        User user = userMapper.fromUserCreateAccountRequestDtoToUserModel(dto);
        assertNotNull(user);
        assertEquals("", user.getEmail());
        assertEquals("", user.getPhoneNumber());
        assertEquals("", user.getFirstName());
    }

    @Test
    void fromUserCreateAccountRequestDtoToUserModel_ShouldHandleMixedValues() {
        UserCreateAccountRequest dto = new UserCreateAccountRequest("test@example.com", "12345678",
                "12345678", "");
        User user = userMapper.fromUserCreateAccountRequestDtoToUserModel(dto);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("", user.getFirstName());
    }

    @Test
    void fromUserModelToUserAccountResponseDto_WithNullUser_ShouldReturnNull() {
        UserAccountResponse response = userMapper.fromUserModelToUserAccountResponseDto(null);
        assertNull(response);
    }

    @Test
    void fromUserModelToUserAccountResponseDto_ShouldMapAllFields() {
        User user = new User();
        UUID id = UUID.randomUUID();
        Timestamp registrationDate = new Timestamp(System.currentTimeMillis());

        user.setId(id);
        user.setEmail("test@example.com");
        user.setPhoneNumber("12345678");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate("22-22-22");
        user.setBirthDate("22-22-22");
        user.setAboutMe("About me text");
        user.setRegistrationDate(registrationDate);
        user.setStatus(UserStatus.ACTIVE);

        Map<String, String> links = new HashMap<>();
        links.put("telegram", "https://github.com/johndoe");
        user.setLinks(links);

        UserAccountResponse response = userMapper.fromUserModelToUserAccountResponseDto(user);

        assertEquals(id, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("12345678", response.getPhoneNumber());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("22-22-22", response.getBirthDate());
        assertEquals("About me text", response.getAboutMe());
        assertEquals(registrationDate, response.getRegistrationDate());
        assertEquals(UserStatus.ACTIVE, response.getStatus());
        assertEquals(1, response.getLinks().size());
        assertEquals("https://github.com/johndoe", response.getLinks().get("telegram"));
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldUpdateAllFields() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setBirthDate("22-22-13");
        dto.setAboutMe("About me text");
        dto.setStatus(UserStatus.ACTIVE);

        Map<String, String> links = new HashMap<>();
        links.put("telegram", "https://github.com/johndoe");
        dto.setLinks(links);

        User user = new User();

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertNotNull(user.getBirthDate());
        assertEquals("About me text", user.getAboutMe());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(1, user.getLinks().size());
        assertEquals("https://github.com/johndoe", user.getLinks().get("telegram"));
    }

    @Test
    void fromUserModelToUserAccountResponseDto_ShouldHandleEmptyStrings() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("");
        user.setPhoneNumber("");
        user.setFirstName("");
        user.setLastName("");

        UserAccountResponse response = userMapper.fromUserModelToUserAccountResponseDto(user);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("", response.getEmail());
        assertEquals("", response.getPhoneNumber());
        assertEquals("", response.getFirstName());
        assertEquals("", response.getLastName());
    }

    @Test
    void fromUserModelToUserAccountResponseDto_WithNullLinks_ShouldHaveNullLinks() {
        User user = new User();
        user.setLinks(null);

        UserAccountResponse response = userMapper.fromUserModelToUserAccountResponseDto(user);

        assertNull(response.getLinks());
    }

    @Test
    void fromUserModelToUserAccountResponseDto_ShouldHandleNullValues(){
        User user = new User();
        user.setId(null);
        user.setEmail(null);
        user.setPhoneNumber(null);
        user.setFirstName(null);

        UserAccountResponse response = userMapper.fromUserModelToUserAccountResponseDto(user);
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getEmail());
        assertNull(response.getPhoneNumber());
        assertNull(response.getFirstName());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldUpdateFields() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");

        User user = new User();
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_WithNullLinks_ShouldSetLinksToNull() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setLinks(null);

        User user = new User();
        Map<String, String> existingLinks = new HashMap<>();
        existingLinks.put("telegram", "https://github.com/johndoe");
        user.setLinks(existingLinks);
        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertNull(user.getLinks());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_WithExistingLinksAndNonEmptyNewLinks() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        Map<String, String> newLinks = new HashMap<>();
        newLinks.put("telegram", "https://twitter.com/johndoe");
        dto.setLinks(newLinks);

        User user = new User();
        Map<String, String> existingLinks = new HashMap<>();
        existingLinks.put("vk", "https://github.com/johndoe");
        user.setLinks(existingLinks);

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertEquals(1, user.getLinks().size());
        assertEquals("https://twitter.com/johndoe", user.getLinks().get("telegram"));
        assertNull(user.getLinks().get(""));
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_WithEmptyLinks_ShouldClearExistingLinks() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setLinks(new HashMap<>());

        User user = new User();
        Map<String, String> existingLinks = new HashMap<>();
        existingLinks.put("telegram", "https://github.com/johndoe");
        user.setLinks(existingLinks);

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertTrue(user.getLinks().isEmpty());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_WithNewLinks_ShouldUpdateLinks() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        Map<String, String> newLinks = new HashMap<>();
        newLinks.put("telegram", "https://linkedin.com/johndoe");
        dto.setLinks(newLinks);

        User user = new User();
        user.setLinks(null);

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertEquals(1, user.getLinks().size());
        assertEquals("https://linkedin.com/johndoe", user.getLinks().get("telegram"));
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldHandleNullValues() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setEmail(null);
        dto.setPhoneNumber("12345678");

        User user = new User();
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertNull(user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
    }


    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldNotUpdateIgnoredFields() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");

        User user = new User();
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");
        user.setId(UUID.randomUUID());
        user.setRegistrationDate(new Timestamp(System.currentTimeMillis()));

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertNotNull(user.getId());
        assertNotNull(user.getRegistrationDate());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldHandleEmptyStrings() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setEmail("");
        dto.setPhoneNumber("");

        User user = new User();
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertEquals("", user.getEmail());
        assertEquals("", user.getPhoneNumber());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldUpdateNullUserFields() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");

        User user = new User();
        user.setEmail(null);
        user.setPhoneNumber(null);

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
    }


    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldHandleAllFields() {
        UserUpdateAccountRequest dto = new UserUpdateAccountRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");
        dto.setFirstName("Jane");
        dto.setLastName("Smith");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRegistrationDate(new Timestamp(System.currentTimeMillis()));

        userMapper.updateUserModelFromUpdateAccountRequestDto(dto, user);

        assertNotNull(user.getId());
        assertNotNull(user.getRegistrationDate());

        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
    }

}
