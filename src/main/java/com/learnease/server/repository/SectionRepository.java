package com.learnease.server.repository;

import com.learnease.server.dto.course.ShowSectionsResDto;
import com.learnease.server.model.Section;
import com.learnease.server.model.enums.ContentStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section, UUID> {
//    Uni-directional mapping query
//    @Query("""
//        SELECT COALESCE(MAX(s.sectionNumber), 0)
//        FROM Section s
//        WHERE s.id IN (
//            SELECT sec.id FROM Course c JOIN c.sections sec WHERE c.id = :courseId
//        )
//    """)
//    int findMaxSectionNumberByCourseId(@Param("courseId") UUID courseId);

    @Query("SELECT COALESCE(MAX(s.sectionNumber), 0) FROM Section s WHERE s.course.id = :courseId")
    int findMaxSectionNumberByCourseId(UUID courseId);

    List<Section> findByCourseIdAndStatusOrderBySectionNumber(
            UUID courseId,
            ContentStatus status
    );

    Optional<Section> findByIdAndCourseId(UUID sectionId, UUID courseId);



    @Query(
            """
                    select new com.learnease.server.dto.course.ShowSectionsResDto(
                    s.id,
                    s.sectionNumber,
                    s.title,
                    s.description
                    ) from Section s
                    where s.course.id = :courseId
                    ORDER BY s.sectionNumber
                    """
    )
    List<ShowSectionsResDto> getAllSections(@Param("courseId") UUID courseId);
}
