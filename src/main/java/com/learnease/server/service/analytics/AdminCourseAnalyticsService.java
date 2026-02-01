package com.learnease.server.service.analytics;

import com.learnease.server.dto.admin.*;
import com.learnease.server.model.Course;
import com.learnease.server.model.enums.BookingStatus;
import com.learnease.server.repository.AdminCourseAnalyticsRepository;
import com.learnease.server.repository.BookingRepository;
import com.learnease.server.repository.CategoryRepository;
import com.learnease.server.repository.FeedbackRepository;
import com.learnease.server.util.enums.CourseSortField;
import com.learnease.server.util.mappers.CourseSortMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCourseAnalyticsService {

    private final AdminCourseAnalyticsRepository repository;
    private final CategoryRepository categoryRepository;
    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;

    public Page<AdminCourseOverviewDto> getCoursesOverview(
            int page,
            int size,
            CourseSortField sortField,
            Sort.Direction direction
    ) {

        Sort sort = CourseSortMapper.map(sortField, direction);
        Pageable pageable = PageRequest.of(page, size, sort);

        LocalDateTime recentDate = LocalDateTime.now().minusDays(7);

        Page<AdminCourseOverviewDto> pageResult =
                repository.fetchCourseOverview(recentDate, BookingStatus.PAID.name(), pageable)
                        .map(p -> new AdminCourseOverviewDto(
                                p.getCourseId(),
                                p.getCourseName(),
                                p.getCategoryId(),
                                p.getCategoryName(),
                                p.getPrice(),
                                p.getDiscount(),
                                p.getEffectivePrice(),
                                p.getTotalEnrollments(),
                                p.getRecentEnrollments(),
                                p.getTotalRevenue(),
                                p.getAvgRevenuePerStudent(),
                                p.getAvgRating(),
                                p.getTotalFeedbacks(),
                                p.getInstructorId(),
                                p.getInstructorName(),
                                p.getDurationInHours(),
                                p.getRevenueRank(),
                                p.getCreatedAt()
                        ));

        long startRank = (long) page * size + 1;

        List<AdminCourseOverviewDto> rankedList = new ArrayList<>();
        long currentRank = startRank;

        for (AdminCourseOverviewDto dto : pageResult.getContent()) {
            dto.setRevenueRank(currentRank++);
            rankedList.add(dto);
        }

        return new PageImpl<>(
                rankedList,
                pageable,
                pageResult.getTotalElements()
        );
    }

    public List<CategoryDistributionDTO> getCategoryDistribution(){
        return categoryRepository.getCategoryDistribution();
    }

    public List<CourseRatingDTO> getTopCourseRatings(int top){
        return feedbackRepository.findTopCoursesByRating(top);
    }

    public List<CourseRevenueDTO> getTopCoursesByRevenue(int top){
        return bookingRepository.findTopCourseByRevenue(BookingStatus.PAID, Pageable.ofSize(top));
    }
}

