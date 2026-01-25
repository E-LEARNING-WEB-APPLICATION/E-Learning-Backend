package com.learnease.server.service;

import com.learnease.server.dto.admin.MonthlyRevenueDTO;
import com.learnease.server.dto.admin.MonthlyStudentEnrollmentDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AdminStatisticsService {
    List<MonthlyRevenueDTO> getRevenueByMonthAfterDate(int numberOfMonthsAgo);

    List<MonthlyRevenueDTO> getInstructorRevenueByMonthAfterDate(UUID instructorId, int numberOfMonthsAgo);

    BigDecimal getCurrentMonthRevenue();

    BigDecimal getPreviousMonthRevenue();

    List<MonthlyStudentEnrollmentDTO> getMonthlyStudentEnrollments(int monthsBack);
}
