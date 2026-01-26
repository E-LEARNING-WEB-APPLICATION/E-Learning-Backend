package com.learnease.server.util.mappers;


import com.learnease.server.dto.CourseInstructorResponseDto;
import com.learnease.server.model.Instructor;
import org.springframework.stereotype.Component;

@Component
public class InstructorMapper {

    public CourseInstructorResponseDto toResponse(Instructor instructor) {

        CourseInstructorResponseDto dto = new CourseInstructorResponseDto();
        dto.setFname(instructor.getUserDetails().getFirstName());
        dto.setLname(instructor.getUserDetails().getLastName());
        dto.setBio(instructor.getBio());
        dto.setProfilePic(instructor.getUserDetails().getProfilePic());
        dto.setExperience(instructor.getExperience());
        CourseInstructorResponseDto.Socials socials =
                new CourseInstructorResponseDto.Socials();
        socials.setLinkedin(instructor.getLinkedInUrl());
        socials.setTwitter(instructor.getTwitterUrl());
        socials.setGithub(instructor.getGitHubUrl());

        dto.setSocials(socials);
        return dto;
    }
}
