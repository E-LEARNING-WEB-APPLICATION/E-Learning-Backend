package com.learnease.server.service.impl;

import com.learnease.server.dto.student.ProfileDto;
import com.learnease.server.model.UserDetails;
import com.learnease.server.repository.StudentRepository;
import com.learnease.server.repository.UserAuthRepository;
import com.learnease.server.repository.UserDetailRepository;
import com.learnease.server.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserAuthRepository userAuthRepository;
    private final StudentRepository studentRepository;
    private final UserDetailRepository userDetailRepository;

    @Override
    public ProfileDto getStudentDetails(Long UserId) {

//        UserDetails userDetails = userDetailRepository.getById(Id);
        return null;
    }
}
