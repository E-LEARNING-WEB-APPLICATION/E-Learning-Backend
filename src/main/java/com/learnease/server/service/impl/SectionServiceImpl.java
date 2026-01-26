package com.learnease.server.service.impl;

import com.learnease.server.dto.course.AddSectionRequestDto;
import com.learnease.server.dto.course.SectionResponseDto;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.model.Course;
import com.learnease.server.model.Section;
import com.learnease.server.model.enums.ContentStatus;
import com.learnease.server.repository.CourseRepository;
import com.learnease.server.repository.SectionRepository;
import com.learnease.server.service.SectionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional
public class SectionServiceImpl implements SectionService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    @Override
    public SectionResponseDto addSection(UUID courseId, AddSectionRequestDto dto) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        int nextSectionNumber =
                sectionRepository.findMaxSectionNumberByCourseId(courseId) + 1;

        Section section = new Section();
        section.setTitle(dto.getTitle());
        section.setDescription(dto.getDescription());
        section.setSectionNumber(nextSectionNumber);
        section.setCourse(course); // bidirectional

        sectionRepository.save(section);

        return SectionResponseDto.toDto(section);
    }

    @Override
    public List<SectionResponseDto> getSectionsByCourse(UUID courseId) {

        return sectionRepository
                .findByCourseIdAndStatusOrderBySectionNumber(
                        courseId, ContentStatus.ACTIVE
                )
                .stream()
                .map(SectionResponseDto::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteSection(UUID courseId, UUID sectionId) {
        Section section = sectionRepository.findByIdAndCourseId(sectionId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        section.setStatus(ContentStatus.DELETED);

        // Optional: cascade soft delete topics
        section.getTopics().forEach(
                topic -> topic.setStatus(ContentStatus.DELETED)
        );
    }

}
