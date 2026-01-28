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
public class AddSectionReqDto {

    UUID courseId ;
    String courseName ;
    String sectionDesc;
    int sectionNumber;
    String sectionTitle;

}
