package com.learnease.server.service.impl;

import com.learnease.server.dto.admin.*;
import com.learnease.server.exception.custom_exception.BadClientRequestException;
import com.learnease.server.model.enums.BookingStatus;
import com.learnease.server.repository.BookingRepository;
import com.learnease.server.repository.FeedbackRepository;
import com.learnease.server.repository.InstructorRepository;
import com.learnease.server.service.InstructorStatisticsService;
import com.learnease.server.util.enums.InstructorSortBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorStatisticsServiceImpl implements InstructorStatisticsService {
    private final BookingRepository bookingRepository;
    private final InstructorRepository instructorRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public List<InstructorMonthlyRevenueDTO> getTopInstructorByMonthlyRevenue(int top) {
        return bookingRepository.findTopInstructorsByRevenue(BookingStatus.PAID, top);
    }

    @Override
    public List<InstructorEnrollmentsDTO> getTopInstructorByEnrollments(int top){
        return bookingRepository.findTopInstructorsByEnrollments(BookingStatus.PAID, top);
    }

    public List<CourseRevenueDTO> getTopCoursesByRevenue(UUID instructorId, int top){
        instructorRepository.findById(instructorId).orElseThrow(()->new BadClientRequestException("Instructor Id not valid"));
        return bookingRepository.findTopCourseByRevenueAndInstructorId(BookingStatus.PAID, instructorId, Pageable.ofSize(top));
    }

    public List<RatingCountDTO> getInstructorRatingDistribution(UUID instructorId){
        return feedbackRepository.findRatingDistributionByInstructor(instructorId).stream()
                .map(dto -> new RatingCountDTO(dto.getRating(), dto.getCount()))
                .toList();
    }

    @Override
    public List<InstructorLeaderboardDTO> getTopInstructors(
            InstructorSortBy sortBy,
            int limit
    ) {
        return bookingRepository.findTopInstructors(
                BookingStatus.PAID.name(),
                sortBy.name(),
                limit
        );
    }


}
