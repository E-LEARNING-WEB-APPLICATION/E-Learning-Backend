package com.learnease.server.repository;

import com.learnease.server.model.Topic;
import com.learnease.server.model.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
