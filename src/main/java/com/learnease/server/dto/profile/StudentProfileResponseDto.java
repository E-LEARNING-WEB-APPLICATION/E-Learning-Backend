package com.learnease.server.dto.profile;

import com.learnease.server.model.Address;
import com.learnease.server.model.Education;
import com.learnease.server.model.Skill;
import com.learnease.server.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentProfileResponseDto {
    private String email;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private Gender gender;

    private String phoneNo;

    private String profilePic;

    private List<Education> educations = new ArrayList<>();

    private Address address;

    private Set<Skill> skills = new HashSet<>();
}
