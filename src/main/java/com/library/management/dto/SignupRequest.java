package com.library.management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank @Size(max = 50) String username,
    @NotBlank @Size(min = 8, max = 72) String password,
    @NotBlank @Size(max = 100) String fullName,
    @Email @NotBlank @Size(max = 100) String email,
    @Size(max = 15) String phone
) {}
