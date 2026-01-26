package com.learnease.server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyStudentEnrollmentDTO {
    private int year;
    private int month;
    private long enrolledCount;
}

