package com.jmp.userservice.dto.request;

import com.jmp.userservice.constant.UserStatus;
import com.jmp.userservice.validation.annotation.ValidLinks;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Map;

@Data
public class UserUpdateAccountRequest {

    @NotBlank(message = "Should not be empty")
    @Email(message = "This email is not correct format")
    private String email;

    @NotBlank(message = "Should not be empty")
    @Size(min = 10, max = 15, message = "The phone number should be between 10 and 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Should not be empty")
    @Size(min = 2, max = 20, message = "The first name should be between 2 and 20 characters")
    private String firstName;

    @NotBlank(message = "Should not be empty")
    @Size(min = 2, max = 30, message = "The last name should be between 2 and 30 characters")
    private String lastName;

    @NotBlank(message = "Should not be empty")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Birth date must be in format YYYY-MM-DD"
    )
    private String birthDate;

    @Size(max = 500, message = "The about me section should not exceed 500 characters")
    private String aboutMe;

    @ValidLinks
    private Map<@NotBlank(message = "Link name cannot be empty") String,
            @NotBlank(message = "Link URL cannot be empty") String> links;

    @NotNull(message = "User status cannot be null")
    private UserStatus status;
}
