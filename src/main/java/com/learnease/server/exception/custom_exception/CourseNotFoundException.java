package com.learnease.server.exception.custom_exception;

import java.util.UUID;

public class CourseNotFoundException extends RuntimeException{

    public CourseNotFoundException(UUID courseId) {
        super("Course not found with id: " + courseId);
    }
}
