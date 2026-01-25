package com.learnease.server.dto.profile;

import com.learnease.server.model.Address;
import com.learnease.server.model.Education;
import com.learnease.server.model.Specialization;
import com.learnease.server.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstructorProfileResponseDto {

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private Gender gender;

    private String phoneNo;

    private String profilePic;

    private List<Education> educations = new ArrayList<>();

    private Address address;

    private String bio;

    private String experience;

    private String gitHubUrl;

    private String linkedInUrl;

    private String twitterUrl;

    private Set<Specialization> specializations = new HashSet<>();
}

