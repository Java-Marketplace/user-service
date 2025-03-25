package com.jmp.userservice.dto.request;

import com.jmp.userservice.model.SocialLink;
import com.jmp.userservice.model.UserStatus;
import com.jmp.userservice.validation.annotation.ValidLinks;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

    @NotBlank(message = "Should not be empty")
    @Email(message = "This email is not correct format")
    private String email;

    @NotBlank(message = "Should not be empty")
    @Pattern(regexp = "^\\+?\\d{9,15}$", message = "Invalid phone number format")
    @Size(min = 10, max = 15, message = "The phone number should be between 10 and 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Should not be empty")
    @Size(min = 2, max = 20, message = "The first name should be between 2 and 20 characters")
    private String firstName;

    @NotBlank(message = "Should not be empty")
    @Size(min = 2, max = 30, message = "The last name should be between 2 and 30 characters")
    private String lastName;

    @NotNull(message = "Should not be empty")
    private LocalDate birthday;

    @Size(max = 500, message = "The about me section should not exceed 500 characters")
    private String aboutMe;

    @ValidLinks
    private List<SocialLink> links;

    @NotNull(message = "User status cannot be null")
    private UserStatus status;
}
