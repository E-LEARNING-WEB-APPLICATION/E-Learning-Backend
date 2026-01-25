package com.learnease.server.dto.Profile;

import com.learnease.server.model.Address;
import com.learnease.server.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstructorProfileRequestDto {
    @NotBlank(message = "Email can not be blank")
    private String email;
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
    @NotNull(message = "Gender must not empty")
    private Gender gender;
    @NotNull(message = "Phone number cannot be null")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    private String phoneNo;
    @NotNull(message = "Address must be filled")
    private Address address;
    @NotNull(message = "bio must be filled")
    private String bio;
    @NotNull(message = "experience must be filled")
    private String experience;
    private String gitHubUrl;
    private String linkedInUrl;
    private String twitterUrl;

}
