package com.jmp.userservice.dto.response;

import com.jmp.userservice.constant.UserStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Data
public class UserAccountResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String aboutMe;
    private Map<String, String> links;
    private Timestamp registrationDate;
    private UserStatus status;
}
