package com.learnease.server.service.impl;

import com.learnease.server.dto.admin.InstructorEnrollmentsDTO;
import com.learnease.server.dto.admin.InstructorMonthlyRevenueDTO;
import com.learnease.server.model.enums.BookingStatus;
import com.learnease.server.repository.BookingRepository;
import com.learnease.server.service.InstructorStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorStatisticsServiceImpl implements InstructorStatisticsService {
    private final BookingRepository bookingRepository;

    @Override
    public List<InstructorMonthlyRevenueDTO> getTopInstructorByMonthlyRevenue(int top) {
        return bookingRepository.findTopInstructorsByRevenue(BookingStatus.PAID, top);
    }

    @Override
    public List<InstructorEnrollmentsDTO> getTopInstructorByEnrollments(int top){
        return bookingRepository.findTopInstructorsByEnrollments(BookingStatus.PAID, top);
    }
}
