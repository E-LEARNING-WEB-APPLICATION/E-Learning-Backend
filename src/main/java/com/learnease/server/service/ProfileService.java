package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.Profile.*;
import com.learnease.server.model.Skill;
import com.learnease.server.model.Specialization;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProfileService {
     StudentProfileResponseDto getStudentDetails(UUID authId);

    ApiResponse addEducation(UUID authId, EducationRequestDto educationRequestDto);

    ApiResponse updateEducation(UUID authId, EducationRequestDto educationRequestDto);

    ApiResponse deleteEducation(UUID authId, UUID educationId);

    List<Skill> getAllSkills();

    ApiResponse updateSkill(UUID authId, SkillRequestDto skillRequestDto);

    ApiResponse updateStudentProfile(UUID authId, @Valid StudentProfileRequestDto studentProfileRequestDto);

    ApiResponse updateProfilePic(UUID authId, MultipartFile profilePic);

    InstructorProfileResponseDto getInstructorDetails(UUID authId);

    List<Specialization> getAllSpecialization();

    ApiResponse updateSpecialization(UUID authId, SpecializationRequestDto specializationRequestDto);

    ApiResponse updateInstructorProfile(UUID authId, @Valid InstructorProfileRequestDto instructorProfileRequestDto);
}
