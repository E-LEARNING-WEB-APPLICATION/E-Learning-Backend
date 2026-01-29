package com.learnease.server.dto.auth;

import com.learnease.server.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AdminUpdateProfileRequest(
        // User details
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be 2–50 characters")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be 2–50 characters")
        String lastName,
        LocalDate dob,
        Gender gender,
        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Phone number must be a valid 10-digit Indian mobile number"
        )
        String phoneNo,
        String bio,

        // Address
        AddressDto address
    ) {
    }
