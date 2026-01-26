package com.learnease.server.service;

import com.learnease.server.dto.course.AddSectionRequestDto;
import com.learnease.server.dto.course.SectionResponseDto;

import java.util.List;
import java.util.UUID;

public interface SectionService {
    SectionResponseDto addSection(UUID courseId, AddSectionRequestDto sectionRequestDto);
    List<SectionResponseDto> getSectionsByCourse(UUID courseId);
    void deleteSection(UUID courseId, UUID sectionId);
}
