package com.learnease.server.dto.admin;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class CourseEnrollmentDTO {
    private UUID courseId;
    private String title;
    private String Instructor;
    private long enrollments;
    private Double rating;
}
