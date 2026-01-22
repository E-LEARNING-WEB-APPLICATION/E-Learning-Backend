package com.learnease.server.service.impl;

import com.learnease.server.dto.student.StudentProfileResponseDto;
import com.learnease.server.model.Student;
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

    private final StudentRepository studentRepository;
    private final UserDetailRepository userDetailRepository;

    @Override
    public StudentProfileResponseDto getStudentDetails(UUID UserId) {


        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(UserId)
                .orElseThrow();
        Student  student = studentRepository.findByUserDetails_Id(userDetails.getId())
                .orElseThrow();
        StudentProfileResponseDto studentProfileResponseDto = new StudentProfileResponseDto();

        studentProfileResponseDto.setFirstName(userDetails.getFirstName());
        studentProfileResponseDto.setLastName(userDetails.getLastName());
        studentProfileResponseDto.setAddress(userDetails.getAddress());
        studentProfileResponseDto.setEmail(userDetails.getUserAuth().getEmail());
        studentProfileResponseDto.setPhoneNo(userDetails.getPhoneNo());
        studentProfileResponseDto.setDob(userDetails.getDob());
        studentProfileResponseDto.setGender(userDetails.getGender());
        studentProfileResponseDto.setEducations(userDetails.getEducations());
        studentProfileResponseDto.setSkills(student.getSkills());
        studentProfileResponseDto.setProfilePic(userDetails.getProfilePic());


        return studentProfileResponseDto;
    }
}
