package com.sample.busbooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @NotBlank(message ="Username is required")
    private String username;

    @Email(message = "Email is required")
    private String email;

    @NotBlank(message = "MobileNo is required")
    private String mobileNumber;

    @NotBlank(message = " Password is required")
    private String password;

    private String role = "USER"; // Optional: default to USER
}