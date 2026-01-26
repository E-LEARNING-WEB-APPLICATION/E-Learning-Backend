package com.learnease.server.service.impl;

import com.learnease.server.dto.course.AddTopicRequestDto;
import com.learnease.server.dto.course.TopicResponseDto;
import com.learnease.server.exception.custom_exception.FileStorageException;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.model.Section;
import com.learnease.server.model.Topic;
import com.learnease.server.model.enums.ContentStatus;
import com.learnease.server.repository.SectionRepository;
import com.learnease.server.repository.TopicRepository;
import com.learnease.server.service.TopicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicServiceImpl implements TopicService {

    private final SectionRepository sectionRepository;
    private final TopicRepository topicRepository;
    private final S3Service s3Service;

    @Override
    public TopicResponseDto addTopic(UUID sectionId, AddTopicRequestDto dto) {

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        int nextTopicNumber =
                topicRepository.findMaxTopicNumberBySectionId(sectionId) + 1;

        String videoUrl;
        try {
            videoUrl = s3Service.uploadFile(dto.getVideo(), "topics/videos");
        } catch (IOException e) {
            throw new FileStorageException("Failed to upload video");
        }

        Topic topic = new Topic();
        topic.setTitle(dto.getTitle());
        topic.setDescription(dto.getDescription());
        topic.setNotes(dto.getNotes());
        topic.setHour(dto.getHour());
        topic.setMin(dto.getMin());
        topic.setVideo(videoUrl);
        topic.setTopicNumber(nextTopicNumber);
        topic.setSection(section);

        topicRepository.save(topic);

        return TopicResponseDto.toDto(topic);
    }

    @Override
    public List<TopicResponseDto> getTopicsBySection(UUID sectionId) {

        return topicRepository.findBySectionIdAndStatusOrderByTopicNumber(sectionId, ContentStatus.ACTIVE)
                .stream()
                .map(TopicResponseDto::toDto)
                .toList();
    }

    @Transactional
    public void deleteTopic(UUID sectionId, UUID topicId) {

        Topic topic = topicRepository
                .findByIdAndSectionId(topicId, sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        topic.setStatus(ContentStatus.DELETED);
    }


}

