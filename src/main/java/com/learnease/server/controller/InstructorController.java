package com.learnease.server.controller;


import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.CourseInstructorResponseDto;
import com.learnease.server.dto.CoursesDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.course.AddSectionReqDto;
import com.learnease.server.dto.course.AddTopicReqDto;
import com.learnease.server.dto.course.ShowSectionsResDto;
import com.learnease.server.dto.course.TopicResponseDto;
import com.learnease.server.dto.instructor.DashboardInstructorResponseDto;
import com.learnease.server.model.Course;
import com.learnease.server.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
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
import java.util.UUID;

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
            @RequestParam UUID categoryId,
            @RequestParam MultipartFile image,
            @RequestParam MultipartFile video,
            @AuthenticationPrincipal JWTDTO user
    )
    {

       ApiResponse response = instructorService.addCourse(courseName,courseDesc, fees, discountPercentage, hour, categoryId,image, video, user);
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

    @Operation(
            summary = "Get instructor profile",
            description = "Fetch logged-in instructor profile details"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getInstructorById(
            @PathVariable UUID id
    ) {
        CourseInstructorResponseDto response = instructorService.getInstructorById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<CourseInstructorResponseDto>(true , response));
    }

    @Operation(
            summary = "Get all the Instuctor",
            description = "Fetch all superficial inforamtion about Instuctor to display on dashboard"
    )
    @GetMapping("/getAllInstructor")
    public ResponseEntity<?> getAllInstructor(){
        List<DashboardInstructorResponseDto> list = instructorService.getAllInstructors();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(list);
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("addSection")
    public ResponseEntity<ApiResponse> addSection(@AuthenticationPrincipal JWTDTO jwtdto, @RequestBody AddSectionReqDto addSectionReqDto)
    {
        ApiResponse response = instructorService.addSection(jwtdto.getUserId(),addSectionReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("getCourseSections/{courseId}")
    public ResponseEntity<List<ShowSectionsResDto>> getCourseSections(@AuthenticationPrincipal JWTDTO jwtdto, @PathVariable UUID courseId)
    {
        List<ShowSectionsResDto> response = instructorService.getAllSections(courseId,jwtdto.getUserId());
        return ResponseEntity.status(200).body(response);

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "addTopic",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> addSection(@AuthenticationPrincipal JWTDTO jwtdto, @ModelAttribute AddTopicReqDto addTopicReqDto)
    {
        ApiResponse response = instructorService.addTopic(jwtdto.getUserId(),addTopicReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("getTopics/{sectionId}")
    public ResponseEntity<List<TopicResponseDto>> getTopics(@PathVariable UUID sectionId,@AuthenticationPrincipal JWTDTO jwtdto)
    {
        List<TopicResponseDto> response = instructorService.getTopics(sectionId,jwtdto.getUserId());
        return ResponseEntity.status(200).body(response);
    }


}
