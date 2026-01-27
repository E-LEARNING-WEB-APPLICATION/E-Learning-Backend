package com.learnease.server.dto.instructor.instructorDashboard;

import lombok.Data;

@Data
public class CourseStudentCountDto {

    private String course;
    private int students;

    // constructors
    public CourseStudentCountDto(String course, int students) {
        this.course = course;
        this.students = students;
    }

    // getters & setters
}
