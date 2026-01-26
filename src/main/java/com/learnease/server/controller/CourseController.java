package com.learnease.server.controller;


import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.course.CourseResponseDto;
import com.learnease.server.dto.course.DashboardCoursesResponseDto;
import com.learnease.server.dto.course.EnrolledCourseResponseDto;
import com.learnease.server.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @Operation(
            summary = "Get All Courses",
            description = "Fetch superficial details to render the dashboard course card"
    )
    @GetMapping("/allCourses")
    public ResponseEntity<?> getAllCourse(){
        List<DashboardCoursesResponseDto> list = courseService.getAllCourses();
        return ResponseEntity.status(HttpStatus.FOUND).body(list);
    }
    @Operation(
            summary = "Get all Courses of given Category",
            description = "Fetch all the courses that are present for the given category id"
    )
    @GetMapping("/getCategoryCourses/{categoryId}")
    public ResponseEntity<?> getCategoryCourse(@PathVariable UUID categoryId){
        List<DashboardCoursesResponseDto> list = courseService.getCategoryCourses(categoryId);
        return ResponseEntity.status(HttpStatus.FOUND).body(list);
    }
    @Operation(
            summary = "Get course payment status by ID",
            description = "Fetch if the course is purchased/free or not purchased "
    )
    @GetMapping("/courseStatus/{courseId}")
    public ResponseEntity<?> getCourseStatus(
            @PathVariable UUID courseId,
            Authentication authentication
    ){

        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();
        ApiResponse response = courseService.getCoursePaymentStatus(courseId,authId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @Operation(
            summary = "Get enrolled courses of logged-in student",
            description = "Fetch all courses in which the authenticated student is enrolled"
    )
    @GetMapping("/my-courses")
    public ResponseEntity<?> getMyEnrolledCourses(
            @AuthenticationPrincipal JWTDTO user
    ) {

        UUID authId = user.getUserId();

        List<EnrolledCourseResponseDto> response =
                courseService.getMyEnrolledCourses(authId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<List<EnrolledCourseResponseDto>>(true , response));
    }


}
