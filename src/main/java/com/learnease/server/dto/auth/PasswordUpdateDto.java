package com.learnease.server.dto.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateDto(
        @NotBlank(message = "old Password is required")
        String oldPassword,
        @NotBlank(message = "new Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String newPassword
) {
}
