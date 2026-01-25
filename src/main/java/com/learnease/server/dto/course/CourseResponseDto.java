package com.learnease.server.dto.course;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponseDto {

    private UUID courseId;
    private UUID iId;

    private String courseName;
    private String courseDesc;

    private double fees;
    private int discountPercentage;

    private String courseThumbnail;
    private String courseIntroVideo;
    private String courseDuration;

    private LocalDateTime createdAt;

    private String category;

    private Double rating;
    private Long numberOfReviews;

    private List<SectionDto> sections;
}
