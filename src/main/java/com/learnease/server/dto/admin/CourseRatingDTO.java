package com.learnease.server.dto.admin;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class CourseRatingDTO {
    private UUID courseId;
    private String title;
    private Double rating;
}
