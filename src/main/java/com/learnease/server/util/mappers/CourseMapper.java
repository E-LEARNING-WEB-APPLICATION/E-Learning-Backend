package com.learnease.server.util.mappers;


import com.learnease.server.dto.course.CourseResponseDto;
import com.learnease.server.dto.course.SectionDto;
import com.learnease.server.dto.course.TopicDto;
import com.learnease.server.model.Course;
import com.learnease.server.model.Section;
import com.learnease.server.model.Topic;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseResponseDto toCourseResponseDto(
            Course course,
            Double rating,
            Long reviewCount
    ){
        if(course == null) return null;

        return CourseResponseDto.builder()
                .courseId(course.getId())
                .iId(course.getInstructor() != null ? course.getInstructor().getId() : null)
                .courseName(course.getTitle())
                .courseDesc(course.getDescription())
                .fees(course.getFees())
                .discountPercentage(course.getDiscount())
                .courseThumbnail(course.getThumbnail())
                .courseIntroVideo(course.getIntroVideo())
                .courseDuration(buildDuration(course.getHour()))
                .createdAt(course.getCreatedAt())
                .rating(rating)
                .numberOfReviews(reviewCount)

                .sections(mapSections(course.getSections()))
                .build();
    }

    private List<SectionDto> mapSections(List<Section> sections){
        if (sections == null || sections.isEmpty()) {
            return Collections.emptyList();
        }

        return sections.stream()
                .map(this::toSectionDto)
                .collect(Collectors.toList());
    };

    private SectionDto toSectionDto(Section section) {
        return SectionDto.builder()
                .sId(section.getId())
                .sectionName(section.getTitle())
                .sectionDesc(section.getDescription())
                .createdAt(section.getCreatedAt())
                .topics(mapTopics(section.getTopics()))
                .build();
    };

    private List<TopicDto> mapTopics(List<Topic> topics) {
        if (topics == null || topics.isEmpty()) {
            return Collections.emptyList();
        }

        return topics.stream()
                .map(this::toTopicDto)
                .collect(Collectors.toList());
    };

    private TopicDto toTopicDto(Topic topic) {
        return TopicDto.builder()
                .tId(topic.getId())
                .topicName(topic.getTitle())
                .topicDesc(topic.getDescription())
                .topicVideo(topic.getVideo())
                .topicNotes(topic.getNotes())
                .createdAt(topic.getCreatedAt())
                .build();
    }

    private String buildDuration(int hours) {
        if (hours <= 0) {
            return "N/A";
        }
        return hours + " hours";
    }
}
