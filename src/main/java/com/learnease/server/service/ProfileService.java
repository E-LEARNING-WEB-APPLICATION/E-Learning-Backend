package com.learnease.server.service;

import com.learnease.server.dto.student.ProfileDto;

import java.util.UUID;

public interface ProfileService {
    public ProfileDto getStudentDetails(Long UserId);
}
