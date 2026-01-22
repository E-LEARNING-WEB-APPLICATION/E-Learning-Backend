package com.learnease.server.service;

import com.learnease.server.dto.student.StudentProfileResponseDto;

import java.util.UUID;

public interface ProfileService {
    public StudentProfileResponseDto getStudentDetails(UUID UserId);
}
