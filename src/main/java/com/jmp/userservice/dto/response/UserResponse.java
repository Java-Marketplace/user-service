package com.jmp.userservice.dto.response;

import com.jmp.userservice.model.SocialLink;
import com.jmp.userservice.model.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String aboutMe;
    private List<SocialLink> links;
    private LocalDateTime registrationDate;
    private UserStatus status;
}
