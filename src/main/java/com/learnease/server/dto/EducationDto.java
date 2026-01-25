package com.learnease.server.dto;

import com.learnease.server.model.Education;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EducationDto {

    private String degree;
    private String fieldOfStudy;
    private String institute;
    private int passingYear;

    public static EducationDto fromEntity(Education education) {
        return EducationDto.builder()
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .institute(education.getInstitute())
                .passingYear(education.getPassingYear())
                .build();
    }
}

