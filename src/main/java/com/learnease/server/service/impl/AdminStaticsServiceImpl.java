package com.learnease.server.service.impl;

import com.learnease.server.dto.admin.MonthlyRevenueDTO;
import com.learnease.server.dto.admin.MonthlyStudentEnrollmentDTO;
import com.learnease.server.model.enums.BookingStatus;
import com.learnease.server.repository.BookingRepository;
import com.learnease.server.repository.StudentRepository;
import com.learnease.server.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminStaticsServiceImpl implements AdminStatisticsService {
    private final BookingRepository bookingRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<MonthlyRevenueDTO> getRevenueByMonthAfterDate(int numberOfMonthsAgo) {

        LocalDateTime startTime =
                LocalDate.now()
                        .minusMonths(numberOfMonthsAgo)
                        .withDayOfMonth(1)
                        .atStartOfDay();
        return bookingRepository.findMonthlyRevenue(startTime, BookingStatus.PAID);
    }

    @Override
    public List<MonthlyRevenueDTO> getInstructorRevenueByMonthAfterDate(UUID instructorId, int numberOfMonthsAgo) {

        LocalDateTime startTime =
                LocalDate.now()
                        .minusMonths(numberOfMonthsAgo)
                        .withDayOfMonth(1)
                        .atStartOfDay();
        return bookingRepository.findMonthlyRevenueByInstructor(startTime, instructorId, BookingStatus.PAID);
    }

    @Override
    public BigDecimal getCurrentMonthRevenue() {
        LocalDate now = LocalDate.now();

        LocalDateTime startOfMonth =
                now.withDayOfMonth(1).atStartOfDay();

        LocalDateTime endOfMonth =
                now.withDayOfMonth(now.lengthOfMonth())
                        .atTime(23, 59, 59);

        return bookingRepository.sumRevenueBetweenDates(
                startOfMonth,
                endOfMonth,
                BookingStatus.PAID
        );
    }

    @Override
    public BigDecimal getPreviousMonthRevenue() {
        LocalDate previousMonth = LocalDate.now().minusMonths(1);

        LocalDateTime startOfPrevMonth =
                previousMonth.withDayOfMonth(1).atStartOfDay();

        LocalDateTime endOfPrevMonth =
                previousMonth.withDayOfMonth(previousMonth.lengthOfMonth())
                        .atTime(23, 59, 59);

        return bookingRepository.sumRevenueBetweenDates(
                startOfPrevMonth,
                endOfPrevMonth,
                BookingStatus.PAID
        );
    }

    @Override
    public List<MonthlyStudentEnrollmentDTO> getMonthlyStudentEnrollments(
            int monthsBack
    ) {
        LocalDateTime startDate =
                LocalDateTime.now().minusMonths(monthsBack);

        return studentRepository.findMonthlyStudentEnrollments(startDate);
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return bookingRepository.findSumPricePaid();
    }


}
