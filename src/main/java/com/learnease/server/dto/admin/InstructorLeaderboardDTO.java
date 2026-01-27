package com.learnease.server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class InstructorLeaderboardDTO {

    private String instructorId;
    private String instructorName;
    private BigDecimal totalRevenue;
    private Long totalEnrollments;
    private Double avgCourseRating;
    private Long totalCourses;
    private Long rankRevenue;
    private Long rankEnrollments;
}

