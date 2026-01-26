package com.learnease.server.dto.course;

import com.learnease.server.model.Section;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionResponseDto {

    private UUID sectionId;
    private Integer sectionNumber;
    private String title;
    private String description;

    private int totalTopics;
    private int totalTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static SectionResponseDto toDto(Section section) {
        SectionResponseDto dto = new SectionResponseDto();

        dto.setSectionId(section.getId());
        dto.setSectionNumber(section.getSectionNumber());
        dto.setTitle(section.getTitle());
        dto.setDescription(section.getDescription());

        dto.setTotalTopics(
                section.getTopics() != null ? section.getTopics().size() : 0
        );

        dto.setCreatedAt(section.getCreatedAt());
        dto.setUpdatedAt(section.getUpdatedAt());

        return dto;
    }
}
