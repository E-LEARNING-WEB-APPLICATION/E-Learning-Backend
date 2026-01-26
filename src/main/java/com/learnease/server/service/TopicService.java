package com.learnease.server.service;

import com.learnease.server.dto.course.AddTopicRequestDto;
import com.learnease.server.dto.course.TopicResponseDto;

import java.util.List;
import java.util.UUID;

public interface TopicService {
    TopicResponseDto addTopic(UUID sectionId, AddTopicRequestDto dto);
    List<TopicResponseDto> getTopicsBySection(UUID sectionId);
    void deleteTopic(UUID sectionId, UUID topicId);
}
