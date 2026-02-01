package com.learnease.server.controller.adminPanel;

import com.learnease.server.dto.admin.*;
import com.learnease.server.service.*;
import com.learnease.server.service.analytics.AdminCourseAnalyticsService;
import com.learnease.server.util.enums.CourseSortField;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Tag(name = "statistic controller")
public class AdminStatisticsController {
    private final AdminStatisticsService statisticsService;
    private final InstructorStatisticsService instructorStatisticsService;
    private final AdminCourseAnalyticsService courseAnalyticsService;
    private final CategoryService categoryService;

    @GetMapping("/revenue/by-month")
    public ResponseEntity<List<MonthlyRevenueDTO>> getRevenueByMonth(
            @RequestParam(required = false, defaultValue = "1m") String duration
    ){
        switch(duration){
            case "3m":
                return ResponseEntity.ok(statisticsService.getRevenueByMonthAfterDate(3));
            case "6m":
                return ResponseEntity.ok(statisticsService.getRevenueByMonthAfterDate(6));
            case "1y":
                return ResponseEntity.ok(statisticsService.getRevenueByMonthAfterDate(12));
            default:
                return ResponseEntity.ok(statisticsService.getRevenueByMonthAfterDate(0));
        }
    }

    @GetMapping("/revenue/instructor/{instructor_id}/by_month")
    public ResponseEntity<List<MonthlyRevenueDTO>> getRevenueByInstructorByMonth(
            @PathVariable UUID instructor_id,
            @RequestParam(required = false, defaultValue = "1m") String duration
    ){
        switch(duration){
            case "3m":
                return ResponseEntity.ok(statisticsService.getInstructorRevenueByMonthAfterDate(instructor_id, 3));
            case "6m":
                return ResponseEntity.ok(statisticsService.getInstructorRevenueByMonthAfterDate(instructor_id, 6));
            case "1y":
                return ResponseEntity.ok(statisticsService.getInstructorRevenueByMonthAfterDate(instructor_id, 12));
            default:
                return ResponseEntity.ok(statisticsService.getInstructorRevenueByMonthAfterDate(instructor_id, 0));
        }
    }
    @GetMapping("/revenue/current-month")
    public ResponseEntity<BigDecimal> getCurrentMonthRevenue() {
        return ResponseEntity.ok(
                statisticsService.getCurrentMonthRevenue()
        );
    }

    @GetMapping("/revenue/previous-month")
    public ResponseEntity<BigDecimal> getPreviousMonthRevenue() {
        return ResponseEntity.ok(
                statisticsService.getPreviousMonthRevenue()
        );
    }

    @GetMapping("/instructors/revenue")
    public ResponseEntity<List<InstructorMonthlyRevenueDTO>> getTopInstructorsByRevenue(@RequestParam(required = false, defaultValue = "10") int top){
        return ResponseEntity.ok(instructorStatisticsService.getTopInstructorByMonthlyRevenue(top));
    }

    @GetMapping("/instructors/enrollments")
    public ResponseEntity<List<InstructorEnrollmentsDTO>> getTopInstructorsByEnrollments(@RequestParam(required = false, defaultValue = "10") int top){
        return ResponseEntity.ok(instructorStatisticsService.getTopInstructorByEnrollments(top));
    }

    @GetMapping("/category/count")
    public ResponseEntity<Long> getCategoryCount(){
        return ResponseEntity.ok(categoryService.getAllCategoryCount());
    }

    @GetMapping("/category/distribution")
    public ResponseEntity<List<CategoryDistributionDTO>> getCategoryDistribution(){
        return ResponseEntity.ok(courseAnalyticsService.getCategoryDistribution());
    }

    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(){
        return ResponseEntity.ok(statisticsService.getTotalRevenue());
    }
}
