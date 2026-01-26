package com.learnease.server.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardCoursesResponseDto {

    private UUID id;
    private UUID categoryId;

    private String thumbnail;
    private String title;

    private Double fees;
    private Double rating;

    private Long reviews;

    private int duration;
    private int discount;

}
