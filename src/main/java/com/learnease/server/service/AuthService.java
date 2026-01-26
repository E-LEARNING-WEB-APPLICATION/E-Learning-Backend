package com.learnease.server.service;


import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.InstructorRegisterRequestDto;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;

public interface AuthService {
    public ApiResponse registerStudent(StudentRegisterRequestDto requestDto);

    ApiResponse registerInstructor(InstructorRegisterRequestDto request);
}
