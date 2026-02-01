package com.learnease.server.controller.adminPanel;

import com.learnease.server.dto.admin.CourseRevenueDTO;
import com.learnease.server.dto.admin.RatingCountDTO;
import com.learnease.server.service.InstructorStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/statistics/instructor")
@RequiredArgsConstructor
public class AdminInstructorStatisticsController {

    private final InstructorStatisticsService instructorStatisticsService;

    @GetMapping("{instructorId}/course/top/revenue")
    public ResponseEntity<List<CourseRevenueDTO>> getTopInstructorCourses(
            @PathVariable UUID instructorId,
            @RequestParam(required = false, defaultValue = "5") int top
            ){
        return ResponseEntity.ok(instructorStatisticsService.getTopCoursesByRevenue(instructorId, top));
    }

    @GetMapping("{instructorId}/rating-distribution")
    public ResponseEntity<List<RatingCountDTO>> getInstructorRatingDistribution(
            @PathVariable UUID instructorId
    ){
        return ResponseEntity.ok(instructorStatisticsService.getInstructorRatingDistribution(instructorId));
    }

}
