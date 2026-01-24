package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.Profile.EducationRequestDto;
import com.learnease.server.dto.Profile.SkillRequestDto;
import com.learnease.server.dto.Profile.StudentProfileRequestDto;
import com.learnease.server.dto.Profile.StudentProfileResponseDto;
import com.learnease.server.model.Skill;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ProfileService {
     StudentProfileResponseDto getStudentDetails(UUID authId);

    ApiResponse addEducation(UUID authId, EducationRequestDto educationRequestDto);

    ApiResponse updateEducation(UUID authId, EducationRequestDto educationRequestDto);

    ApiResponse deleteEducation(UUID authId, UUID educationId);

    List<Skill> getAllSkills();

    ApiResponse updateSkill(UUID authId, SkillRequestDto skillRequestDto);

    ApiResponse updateProfile(UUID authId, @Valid StudentProfileRequestDto studentProfileRequestDto);
}
