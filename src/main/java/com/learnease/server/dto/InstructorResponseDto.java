package com.learnease.server.dto;

import com.learnease.server.dto.auth.AddressDto;
import com.learnease.server.model.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class InstructorResponseDto {

    private UUID instructorId;

    private String firstName;
    private String lastName;
    private String experience;
    private double rating;
    private String bio;
    private String phoneNo;
    private String email;

    private String profilePic;

    private String gitHubUrl;
    private String linkedInUrl;
    private String twitterUrl;

    private LocalDate dob;
    private String gender;

    private AddressDto address;
    private List<EducationDto> educations;

    private Set<String> specializations;
    private int totalCourses;

    /* =====================
       Mapper
       ===================== */
    public static InstructorResponseDto fromEntity(Instructor instructor) {

        UserDetails user = instructor.getUserDetails();

        return InstructorResponseDto.builder()
                .instructorId(instructor.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .experience(instructor.getExperience())
                .rating(instructor.getRating())
                .bio(instructor.getBio())
                .phoneNo(instructor.getUserDetails().getPhoneNo())
                .email(instructor.getUserDetails().getUserAuth().getEmail())
                .profilePic(user.getProfilePic())

                .gitHubUrl(instructor.getGitHubUrl())
                .linkedInUrl(instructor.getLinkedInUrl())
                .twitterUrl(instructor.getTwitterUrl())

                .dob(user.getDob())
                .gender(user.getGender() != null ? user.getGender().name() : null)

                .address(AddressDto.fromEntity(user.getAddress()))
                .educations(
                        user.getEducations()
                                .stream()
                                .map(EducationDto::fromEntity)
                                .toList()
                )

                .specializations(
                        instructor.getSpecializations()
                                .stream()
                                .map(Specialization::getTitle)
                                .collect(Collectors.toSet())
                )

                .totalCourses(
                        instructor.getCourses() != null
                                ? instructor.getCourses().size()
                                : 0
                )
                .build();
    }
}

