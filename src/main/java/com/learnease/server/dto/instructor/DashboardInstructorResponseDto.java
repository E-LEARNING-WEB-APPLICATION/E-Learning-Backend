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
//    id
//    name(first and last name)
//    title(bio)
//    speciality(specialization)
//    no of course
//    no of students
//    image(profilepic)
//    email
//    social{ linkdin,github , twitter}
}
