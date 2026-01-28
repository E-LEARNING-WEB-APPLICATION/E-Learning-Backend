package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.course.AddTopicReqDto;
import com.learnease.server.dto.course.AddTopicRequestDto;
import com.learnease.server.dto.course.TopicResponseDto;
import com.learnease.server.exception.custom_exception.FileStorageException;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.Section;
import com.learnease.server.model.Topic;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.ContentStatus;
import com.learnease.server.repository.InstructorRepository;
import com.learnease.server.repository.SectionRepository;
import com.learnease.server.repository.TopicRepository;
import com.learnease.server.repository.UserDetailRepository;
import com.learnease.server.service.TopicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
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
    private final UserDetailRepository userDetailRepository;
    private final InstructorRepository instructorRepository;

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


    @Transactional
    public ApiResponse saveTopic(UUID userId, AddTopicReqDto reqDto, String videoUrl) {

        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Instructor Not Found"));
        Instructor instructor = instructorRepository.findByUserDetails_Id(userDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));
        Section section =  sectionRepository.findById(reqDto.getSectionId()).orElseThrow(()-> new ResourceNotFoundException("Section Not Found"));
        if(!section.getCourse().getInstructor().getId().equals(instructor.getId()))
        {
            throw new AuthorizationDeniedException("Can't Add Topic Which Not Belongs to You");
        }

        Topic topic = new Topic();
        topic.setTitle(reqDto.getTopicName());
        topic.setTopicNumber(reqDto.getTopicNumber());
        topic.setDescription(reqDto.getTopicDesc());
        topic.setHour(reqDto.getHour());
        topic.setMin(reqDto.getMin());
        topic.setVideo(videoUrl);
        topic.setSection(section);
        topic.setNotes("Notes");
        Topic persistTopic = topicRepository.save(topic);
        return new ApiResponse(true,null);
    }


}

