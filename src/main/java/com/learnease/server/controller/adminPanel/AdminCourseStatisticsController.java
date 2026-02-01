package com.learnease.server.controller.adminPanel;

import com.learnease.server.dto.admin.*;
import com.learnease.server.service.AdminStatisticsService;
import com.learnease.server.service.CategoryService;
import com.learnease.server.service.CourseStatisticsService;
import com.learnease.server.service.InstructorStatisticsService;
import com.learnease.server.service.analytics.AdminCourseAnalyticsService;
import com.learnease.server.util.enums.CourseSortField;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/statistics/course")
@RequiredArgsConstructor
public class AdminCourseStatisticsController {

    private final CourseStatisticsService courseStatisticsService;
    private final AdminCourseAnalyticsService courseAnalyticsService;


    @GetMapping("/enrollments")
    public ResponseEntity<List<CourseEnrollmentDTO>> getTopCoursesByEnrollments(@RequestParam(required = false, defaultValue = "10") int top){
        return ResponseEntity.ok(courseStatisticsService.getTopCoursesByEnrollments(top));
    }

    @GetMapping("/overview")
    public ResponseEntity<Page<AdminCourseOverviewDto>> getCoursesOverview(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "TOTAL_REVENUE") CourseSortField sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return ResponseEntity.ok(
                courseAnalyticsService.getCoursesOverview(page, size, sortBy, direction)
        );
    }


    @GetMapping("/by-rating")
    public ResponseEntity<List<CourseRatingDTO>> getTopCourseRatings(
            @RequestParam(required = false, defaultValue = "10") int top
    ){
        return ResponseEntity.ok(courseAnalyticsService.getTopCourseRatings(top));
    }

    @GetMapping("/by-revenue")
    public ResponseEntity<List<CourseRevenueDTO>> getTopCoursesByRevenue(
            @RequestParam(required = false, defaultValue = "10") int top
    ){
        return ResponseEntity.ok(courseAnalyticsService.getTopCoursesByRevenue(top));
    }

    @GetMapping("/rating-distribution")
    public ResponseEntity<List<RatingCountDTO>> getCourseRatingDistribution(
            @RequestParam UUID courseId
            ){
        return ResponseEntity.ok(courseAnalyticsService.getCourseRatingDistribution(courseId));
    }

    @GetMapping("/conversion-rate")
    public ResponseEntity<Double> getOverallConversionRate(
            @RequestParam(required = false) UUID courseId
    ){
        if(courseId!=null) return ResponseEntity.ok(courseAnalyticsService.getConversionRate(courseId));
        return ResponseEntity.ok(courseAnalyticsService.getConversionRate());
    }

    @GetMapping("/top/conversion-rate")
    public ResponseEntity<List<CourseConversionDTO>> getTopCoursesByConversionRate(
            @RequestParam(required = false, defaultValue = "10") int top
    ){
        return ResponseEntity.ok(courseAnalyticsService.getTopCoursesByConversionRate(top));
    }

}
