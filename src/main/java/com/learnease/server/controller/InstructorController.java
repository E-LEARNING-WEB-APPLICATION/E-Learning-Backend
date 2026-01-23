package com.learnease.server.controller;


import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.CoursesDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.model.Course;
import com.learnease.server.service.InstructorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/instructor/")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }


    @PostMapping(value = "addCourse" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> addCourse(
            @RequestParam String courseName,
            @RequestParam String courseDesc,
            @RequestParam double fees,
            @RequestParam int discountPercentage,
            @RequestParam int hour,
            @RequestParam MultipartFile image,
            @RequestParam MultipartFile video,
            @AuthenticationPrincipal JWTDTO user
    )
    {

       ApiResponse response = instructorService.addCourse(courseName,courseDesc, fees, discountPercentage, hour, image, video, user);
       if(response.isSuccess())
       {
           return ResponseEntity.status(HttpStatus.CREATED).body(response);
       }
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/getAllInstructorCourses")
    public ResponseEntity<?> getInstructorCourse(@AuthenticationPrincipal JWTDTO user)
    {
        List<CoursesDto> courses = instructorService.getAllCourses(user);
        return ResponseEntity.ok(courses);
    }

}
