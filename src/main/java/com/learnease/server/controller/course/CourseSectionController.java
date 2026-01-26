package com.learnease.server.controller.course;

import com.learnease.server.dto.course.AddSectionRequestDto;
import com.learnease.server.dto.course.SectionResponseDto;
import com.learnease.server.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/sections")
@RequiredArgsConstructor
public class CourseSectionController {

    private final SectionService sectionService;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<SectionResponseDto> addSection(
            @PathVariable UUID courseId,
            @RequestBody @Valid AddSectionRequestDto requestDto
    ) {
        SectionResponseDto response =
                sectionService.addSection(courseId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SectionResponseDto>> getSections(
            @PathVariable UUID courseId
    ) {
        return ResponseEntity.ok(
                sectionService.getSectionsByCourse(courseId)
        );
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteTopic(
            @PathVariable UUID courseId,
            @PathVariable UUID sectionId
    ) {
        sectionService.deleteSection(courseId, sectionId);
        return ResponseEntity.noContent().build();
    }
}

