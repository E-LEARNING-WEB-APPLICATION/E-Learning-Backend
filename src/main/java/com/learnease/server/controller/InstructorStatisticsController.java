package com.learnease.server.controller;


import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.instructor.instructorDashboard.CourseStudentCountDto;
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
        description = "Statistics include total no of student,courses,total income,wallet withdraw implementation "
)
public class InstructorStatisticsController {
    private final InstructorDashBoardStatisticsService instructorDashBoardStatisticsService;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "No of Student per Courses for all the courses of the instructor")
    @GetMapping("/studentPerCourse")
    public ResponseEntity<?> getStudentPerCourse(Authentication authentication){

        JWTDTO jwtdto = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwtdto.getUserId();
        List<CourseStudentCountDto> courseStudentCountDto = instructorDashBoardStatisticsService.getStudentPerCourses(authId);

        return ResponseEntity.status(HttpStatus.FOUND).body(courseStudentCountDto);
    }
}
