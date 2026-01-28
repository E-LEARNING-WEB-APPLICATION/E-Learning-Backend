package com.learnease.server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseEnrollmentDTO {
    private UUID courseId;
    private String title;
    private String Instructor;
    private Long enrollments;
    private Double rating;
}
