package com.learnease.server.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseStudentDto {
   private UUID id;
   private String imageUrl;
   private  String courseName;
   private String description;
   private int students;

}
