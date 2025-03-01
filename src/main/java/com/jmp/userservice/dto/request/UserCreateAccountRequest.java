package com.jmp.userservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateAccountRequest {

    @NotBlank(message = "Should not be empty")
    @Email(message = "This email is not correct format")
    private String email;

    @NotBlank(message = "Should not be empty")
    @Size(min = 8, max = 36, message = "The password is not the correct length")
    private String password;

    @NotBlank(message = "Should not be empty")
    @Size(min = 10, max = 15, message = "The phone is not the correct length")
    private String phoneNumber;

    @NotBlank(message = "Should not be empty")
    @Size(min = 2, max = 20, message = "The name is not the correct length")
    private String firstName;
}
