package com.learnease.server.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCourseFilterDto {

    private UUID categoryId;
    private String search;

    // sorting
    private String sortBy; // receives frontend value
}
