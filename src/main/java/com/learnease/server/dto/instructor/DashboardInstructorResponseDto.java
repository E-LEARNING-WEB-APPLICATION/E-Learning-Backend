package com.learnease.server.dto.instructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//for the student dashboard
public class DashboardInstructorResponseDto {
    private UUID id;
    private String name;
    private String title;
    private List<String> speciality;
    private int courses;
    private int student;
    private String image;
    private String email;
    private String gitHub;
    private String linkedIn;
    private String twitter;

}
