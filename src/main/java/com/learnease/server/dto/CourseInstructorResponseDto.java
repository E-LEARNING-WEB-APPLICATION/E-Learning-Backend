package com.learnease.server.dto;

import lombok.Data;

@Data
public class CourseInstructorResponseDto {
    private String fname;
    private String lname;
    private String title;
    private String bio;
    private String profilePic;
    private Socials socials;

    private String experience;

    @Data
    public static class Socials {
        private String linkedin;
        private String twitter;
        private String github;
    }
}
