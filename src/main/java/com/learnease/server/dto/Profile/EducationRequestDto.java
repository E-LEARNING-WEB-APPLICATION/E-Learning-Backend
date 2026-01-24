package com.learnease.server.dto.Profile;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EducationRequestDto {
    @NotNull(message = "Degree is Required")
    private String degree;
    @NotNull(message = "Field of Study is Required")
    private String fieldOfStudy;
    @NotNull(message = "Institute is Required")
    private String institute;
    @NotNull(message = "Year of passing is Required")
    private int passingYear;
}
