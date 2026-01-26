package com.learnease.server.dto.course;

import com.learnease.server.model.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicResponseDto {

    private UUID topicId;
    private int topicNumber;
    private String title;
    private String description;
    private int hour;
    private int min;
    private String videoUrl;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TopicResponseDto toDto(Topic topic) {
        TopicResponseDto dto = new TopicResponseDto();
        dto.setTopicId(topic.getId());
        dto.setTopicNumber(topic.getTopicNumber());
        dto.setTitle(topic.getTitle());
        dto.setDescription(topic.getDescription());
        dto.setHour(topic.getHour());
        dto.setMin(topic.getMin());
        dto.setVideoUrl(topic.getVideo());
        dto.setNotes(topic.getNotes());
        dto.setCreatedAt(topic.getCreatedAt());
        dto.setUpdatedAt(topic.getUpdatedAt());
        return dto;
    }
}

