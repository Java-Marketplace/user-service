package com.jmp.userservice.mapper;

import com.jmp.userservice.dto.request.CreateUserRequest;
import com.jmp.userservice.dto.request.UpdateUserRequest;
import com.jmp.userservice.dto.response.UserResponse;
import com.jmp.userservice.model.SocialLink;
import com.jmp.userservice.model.User;
import com.jmp.userservice.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void toEntity_ShouldMapCorrectly() {
        CreateUserRequest dto = new CreateUserRequest("test@example.com", "12345678",
                "12345678", "John");
        User user = userMapper.toEntity(dto);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("John", user.getFirstName());
    }

    @Test
    void fromUserCreateAccountRequestDtoToUserModel_WithNullDto_ShouldReturnNull() {
        User user = userMapper.toEntity(null);
        assertNull(user);
    }

    @Test
    void toEntity_ShouldHandleNullValues() {
        CreateUserRequest dto = new CreateUserRequest(null, null, null, null);
        User user = userMapper.toEntity(dto);
        assertNotNull(user);
        assertNull(user.getEmail());
        assertNull(user.getPhoneNumber());
        assertNull(user.getFirstName());
    }

    @Test
    void toEntity_ShouldHandleEmptyStrings() {
        CreateUserRequest dto = new CreateUserRequest("", "", "", "");
        User user = userMapper.toEntity(dto);
        assertNotNull(user);
        assertEquals("", user.getEmail());
        assertEquals("", user.getPhoneNumber());
        assertEquals("", user.getFirstName());
    }

    @Test
    void toEntity_ShouldHandleMixedValues() {
        CreateUserRequest dto = new CreateUserRequest("test@example.com", "12345678",
                "12345678", "");
        User user = userMapper.toEntity(dto);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("", user.getFirstName());
    }

    @Test
    void fromUserModelToUserAccountResponseDto_WithNullUser_ShouldReturnNull() {
        UserResponse response = userMapper.toDto(null);
        assertNull(response);
    }

    @Test
    void toDto_ShouldMapAllFields() {
        User user = new User();
        UUID id = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();

        user.setId(id);
        user.setEmail("test@example.com");
        user.setPhoneNumber("12345678");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthday(LocalDate.parse("2022-12-22"));
        user.setBirthday(LocalDate.parse("2022-12-22"));
        user.setAboutMe("About me text");
        user.setRegistrationDate(registrationDate);
        user.setStatus(UserStatus.ACTIVE);

        List<SocialLink> links = new ArrayList<>();
        links.add(new SocialLink("telegram", "https://github.com/johndoe"));
        user.setLinks(links);

        UserResponse response = userMapper.toDto(user);

        assertEquals(id, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("12345678", response.getPhoneNumber());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("2022-12-22", response.getBirthday().toString());
        assertEquals("About me text", response.getAboutMe());
        assertEquals(registrationDate, response.getRegistrationDate());
        assertEquals(UserStatus.ACTIVE, response.getStatus());
        assertEquals(1, response.getLinks().size());

        assertEquals(1, response.getLinks().size());
        SocialLink socialLink = response.getLinks().getFirst();
        assertEquals("telegram", socialLink.getType());
        assertEquals("https://github.com/johndoe", socialLink.getUrl());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldUpdateAllFields() {
        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setBirthday(LocalDate.parse("2022-12-13"));
        dto.setAboutMe("About me text");
        dto.setStatus(UserStatus.ACTIVE);

        List<SocialLink> links = new ArrayList<>();
        links.add(new SocialLink("telegram", "https://github.com/johndoe"));
        dto.setLinks(links);

        User user = new User();

        userMapper.updateModel(dto, user);

        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertNotNull(user.getBirthday());
        assertEquals("About me text", user.getAboutMe());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(1, user.getLinks().size());
        assertEquals(1, user.getLinks().size());

        SocialLink socialLink = user.getLinks().getFirst();
        assertEquals("telegram", socialLink.getType());
        assertEquals("https://github.com/johndoe", socialLink.getUrl());
    }

    @Test
    void toDto_ShouldHandleEmptyStrings() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("");
        user.setPhoneNumber("");
        user.setFirstName("");
        user.setLastName("");

        UserResponse response = userMapper.toDto(user);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("", response.getEmail());
        assertEquals("", response.getPhoneNumber());
        assertEquals("", response.getFirstName());
        assertEquals("", response.getLastName());
    }

    @Test
    void toDto_WithNullLinks_ShouldHaveNullLinks() {
        User user = new User();
        user.setLinks(null);

        UserResponse response = userMapper.toDto(user);

        assertNull(response.getLinks());
    }

    @Test
    void toDto_ShouldHandleNullValues() {
        User user = new User();
        user.setId(null);
        user.setEmail(null);
        user.setPhoneNumber(null);
        user.setFirstName(null);

        UserResponse response = userMapper.toDto(user);
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getEmail());
        assertNull(response.getPhoneNumber());
        assertNull(response.getFirstName());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldUpdateFields() {
        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");

        User user = new User();
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");

        userMapper.updateModel(dto, user);

        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
    }

    @Test
    void updateModel_WithNullLinks_ShouldSetLinksToNull() {
        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setLinks(null);

        User user = new User();
        List<SocialLink> existingLinks = new ArrayList<>();
        existingLinks.add(new SocialLink("telegram", "https://github.com/johndoe"));
        user.setLinks(existingLinks);

        userMapper.updateModel(dto, user);

        assertNull(user.getLinks());
    }

    @Test
    void updateModel_WithExistingLinksAndNonEmptyNewLinks() {
        UpdateUserRequest dto = new UpdateUserRequest();
        List<SocialLink> newLinks = new ArrayList<>();
        newLinks.add(new SocialLink("telegram", "https://twitter.com/johndoe"));
        dto.setLinks(newLinks);

        User user = new User();
        List<SocialLink> existingLinks = new ArrayList<>();
        existingLinks.add(new SocialLink("vk", "https://github.com/johndoe"));
        user.setLinks(existingLinks);

        userMapper.updateModel(dto, user);

        assertEquals(1, user.getLinks().size());
        SocialLink socialLink = user.getLinks().getFirst();
        assertEquals("https://twitter.com/johndoe", socialLink.getUrl());
    }

    @Test
    void updateModel_WithEmptyLinks_ShouldClearExistingLinks() {
        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setLinks(new ArrayList<>());

        User user = new User();
        List<SocialLink> existingLinks = new ArrayList<>();
        existingLinks.add(new SocialLink("telegram", "https://github.com/johndoe"));
        user.setLinks(existingLinks);

        userMapper.updateModel(dto, user);

        assertTrue(user.getLinks().isEmpty());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_WithNewLinks_ShouldUpdateLinks() {
        UpdateUserRequest dto = new UpdateUserRequest();
        List<SocialLink> newLinks = new ArrayList<>();
        newLinks.add(new SocialLink("telegram", "https://linkedin.com/johndoe"));
        dto.setLinks(newLinks);

        User user = new User();
        user.setLinks(null);

        userMapper.updateModel(dto, user);

        assertEquals(1, user.getLinks().size());
        SocialLink socialLink = user.getLinks().getFirst();
        assertEquals("https://linkedin.com/johndoe", socialLink.getUrl());
    }

    @Test
    void updateModel_ShouldHandleNullValues() {
        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setEmail(null);
        dto.setPhoneNumber("12345678");

        User user = new User();
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");

        userMapper.updateModel(dto, user);

        assertNull(user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
    }


    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldNotUpdateIgnoredFields() {
        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");

        User user = new User();
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");
        user.setId(UUID.randomUUID());
        user.setRegistrationDate(LocalDateTime.now());

        userMapper.updateModel(dto, user);

        assertNotNull(user.getId());
        assertNotNull(user.getRegistrationDate());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
    }

    @Test
    void updateModel_ShouldHandleEmptyStrings() {
        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setEmail("");
        dto.setPhoneNumber("");

        User user = new User();
        user.setEmail("old@example.com");
        user.setPhoneNumber("87654321");

        userMapper.updateModel(dto, user);

        assertEquals("", user.getEmail());
        assertEquals("", user.getPhoneNumber());
    }

    @Test
    void updateUserModelFromUpdateAccountRequestDto_ShouldUpdateNullFields() {
        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("12345678");

        User user = new User();
        user.setEmail(null);
        user.setPhoneNumber(null);

        userMapper.updateModel(dto, user);

        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
    }


    @Test
    void updateModel_ShouldHandleAllFields() {
        UpdateUserRequest dto = new UpdateUserRequest();
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
        user.setRegistrationDate(LocalDateTime.now());

        userMapper.updateModel(dto, user);

        assertNotNull(user.getId());
        assertNotNull(user.getRegistrationDate());

        assertEquals("new@example.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
    }

}
