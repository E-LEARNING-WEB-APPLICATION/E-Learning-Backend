package com.learnease.server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class InstructorMonthlyRevenueDTO {
    private UUID instructorId;
    private String instructorName;
    private BigDecimal revenue;
}
