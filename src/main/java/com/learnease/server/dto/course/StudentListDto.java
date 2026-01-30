package com.learnease.server.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentListDto
{
    private UUID id;
    private String profilePic;
    private String firstName;
    private String lastName;
    private String  dob = "N/A";
    private String gender = "N/A";
    private String phoneNo;
    private String address = "N/A";
    private String coursePurchasedOn;
}
