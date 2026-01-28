package com.learnease.server.repository;

import com.learnease.server.dto.course.TopicResponseDto;
import com.learnease.server.model.Topic;
import com.learnease.server.model.enums.ContentStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {

    @Query("""
        SELECT COALESCE(MAX(t.topicNumber), 0)
        FROM Topic t
        WHERE t.section.id = :sectionId
    """)
    int findMaxTopicNumberBySectionId(UUID sectionId);

    List<Topic> findBySectionIdOrderByTopicNumberAsc(UUID sectionId);

    List<Topic> findBySectionIdAndStatusOrderByTopicNumber(
            UUID sectionId,
            ContentStatus status
    );

    Optional<Topic> findByIdAndSectionId(UUID topicId, UUID sectionId);

    @Query(
            """ 
                    select new com.learnease.server.dto.course.TopicResponseDto
                    (
                    t.id,
                    t.topicNumber,
                    t.title,
                    t.description,
                    t.hour,
                    t.min,
                    t.video,
                    t.notes,
                    t.createdAt,
                    t.updatedAt
                    )
                    from
                    Topic t
                    where t.section.id = :sectionId
                    order by t.topicNumber
                    """
    )
    List<TopicResponseDto> getAllTopics(@Param("sectionId") UUID sectionId);
}
