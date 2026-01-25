package com.learnease.server.service.impl;

import com.learnease.server.model.enums.Status;
import com.learnease.server.repository.StudentRepository;
import com.learnease.server.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;


    @Override
    public long getCountOfStudents() {
        return studentRepository.count();
    }

    @Override
    public long getEnrolledStudentCountAfterDate(LocalDateTime datetime) {
        return studentRepository.countAllByCreatedAtAfterAndUserDetails_UserAuth_Status(datetime, Status.ACTIVE);
    }
}
