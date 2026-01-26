package com.learnease.server.controller.course;

import com.learnease.server.dto.course.AddTopicRequestDto;
import com.learnease.server.dto.course.TopicResponseDto;
import com.learnease.server.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sections/{sectionId}/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TopicResponseDto> addTopic(
            @PathVariable UUID sectionId,
            @ModelAttribute @Valid AddTopicRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(topicService.addTopic(sectionId, dto));
    }

    @GetMapping
    public ResponseEntity<List<TopicResponseDto>> getTopics(
            @PathVariable UUID sectionId
    ) {
        return ResponseEntity.ok(
                topicService.getTopicsBySection(sectionId)
        );
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> deleteTopic(
            @PathVariable UUID sectionId,
            @PathVariable UUID topicId
    ) {
        topicService.deleteTopic(sectionId, topicId);
        return ResponseEntity.noContent().build();
    }
}

