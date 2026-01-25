package com.learnease.server.dto.profile;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EducationRequestDto {
    private UUID id;
    @NotNull(message = "Degree is Required")
    private String degree;
    @NotNull(message = "Field of Study is Required")
    private String fieldOfStudy;
    @NotNull(message = "Institute is Required")
    private String institute;
    @NotNull(message = "Year of passing is Required")
    private int passingYear;
}
