package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.Profile.EducationRequestDto;
import com.learnease.server.dto.Profile.StudentProfileResponseDto;

import java.util.UUID;

public interface ProfileService {
     StudentProfileResponseDto getStudentDetails(UUID authId);

    ApiResponse addEducation(UUID authId, EducationRequestDto educationRequestDto);

    ApiResponse updateEducation(UUID authId, EducationRequestDto educationRequestDto);

    ApiResponse deleteEducation(UUID authId, UUID educationId);
}
