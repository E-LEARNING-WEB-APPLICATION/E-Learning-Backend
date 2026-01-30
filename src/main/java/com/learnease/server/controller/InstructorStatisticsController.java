package com.learnease.server.controller;


import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.instructor.instructorDashboard.CategoryCoursesCountDto;
import com.learnease.server.dto.instructor.instructorDashboard.CourseStudentCountDto;
import com.learnease.server.dto.instructor.instructorDashboard.DashboardStatisticsDto;
import com.learnease.server.service.InstructorDashBoardStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/instructorStatistics")
@RequiredArgsConstructor
@Tag(
        name = "Instructor Dashboard statistics api",
        description = "Statistics include total no of student,courses,total income,wallet withdraw implementation,etc"
)
public class InstructorStatisticsController {
    private final InstructorDashBoardStatisticsService instructorDashBoardStatisticsService;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "No of Student per Courses for all the courses of the instructor")
    @GetMapping("/studentPerCourse")
    public ResponseEntity<?> getStudentPerCourse(Authentication authentication){

        JWTDTO jwtdto = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwtdto.getUserId();
        List<CourseStudentCountDto> list = instructorDashBoardStatisticsService.getStudentPerCourses(authId);

        return ResponseEntity.status(HttpStatus.FOUND).body(list);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "No of Courses per Category for all the courses of the instructor")
    @GetMapping("/coursesPerCategory")
    public ResponseEntity<?> getCoursesPerCategory(Authentication authentication){
        JWTDTO jwtdto = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwtdto.getUserId();

        List<CategoryCoursesCountDto> list = instructorDashBoardStatisticsService.getCoursesPerCategory(authId);

        return ResponseEntity.status(HttpStatus.FOUND).body(list);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Api to fetch the statistics of instructor")
    @GetMapping("/stats")
    public ResponseEntity<?> getOverallStat(Authentication authentication){
        JWTDTO jwtdto = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwtdto.getUserId();

        DashboardStatisticsDto dashboardStatisticsDto = instructorDashBoardStatisticsService.getOverAllStat(authId);
        return ResponseEntity.status(HttpStatus.FOUND).body(dashboardStatisticsDto);
    }
}
