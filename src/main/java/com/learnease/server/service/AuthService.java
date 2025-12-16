package com.learnease.server.service;


import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthService {
    public ApiResponse studentRegistrationService(StudentRegisterRequestDto requestDto);
}
