package com.learnease.server.dto.auth;

import com.learnease.server.model.enums.Gender;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record AdminRegisterRequest(
        // Auth
        @Email String email,
        String password,

        // User details
        String firstName,
        String lastName,
        LocalDate dob,
        Gender gender,
        String phoneNo,
        String bio,

        // Address
        AddressDto address
) {
}
