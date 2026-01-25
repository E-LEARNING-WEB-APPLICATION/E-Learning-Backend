package com.learnease.server.controller;


import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.course.CourseResponseDto;
import com.learnease.server.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(
        name = "Courses",
        description = "APIs related to course retrieval and course details"
)
public class CourseController {

    private final CourseService courseService;

    @Operation(
            summary = "Get course by ID",
            description = "Fetch complete course details including sections, topics, category, rating, and reviews"
    )
    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourseById(
            @PathVariable UUID courseId,
            @AuthenticationPrincipal JWTDTO user
            ){
        CourseResponseDto response = courseService.getCourseById(courseId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }
}
