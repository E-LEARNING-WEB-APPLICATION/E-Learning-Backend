package com.learnease.server.dto.instructor.instructorDashboard;

import lombok.Data;

@Data
public class CategoryCoursesCountDto {

    private String name;
    private int value;

    public CategoryCoursesCountDto (String name,int value){
        this.name = name;
        this.value = value;
    }
}
