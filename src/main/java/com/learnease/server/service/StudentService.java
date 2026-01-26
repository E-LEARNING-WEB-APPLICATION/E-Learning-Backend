package com.learnease.server.service;

import java.time.LocalDateTime;

public interface StudentService {
    public long getCountOfStudents();
    public long getEnrolledStudentCountAfterDate(LocalDateTime datetime);
}
