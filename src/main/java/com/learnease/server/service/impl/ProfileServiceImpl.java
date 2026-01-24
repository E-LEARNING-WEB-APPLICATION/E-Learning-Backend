package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.Profile.EducationRequestDto;
import com.learnease.server.dto.Profile.StudentProfileResponseDto;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.Education;
import com.learnease.server.model.Student;
import com.learnease.server.model.UserDetails;
import com.learnease.server.repository.StudentRepository;
import com.learnease.server.repository.UserDetailRepository;
import com.learnease.server.service.ProfileService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final StudentRepository studentRepository;
    private final UserDetailRepository userDetailRepository;

    @Override
    public StudentProfileResponseDto getStudentDetails(UUID authId) {


        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("No Such User Exist"));
        Student  student = studentRepository.findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("No Such Student Exist"));
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

    @Override
    public ApiResponse addEducation(UUID authId, EducationRequestDto educationRequestDto) {

        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(()-> new UserNotFoundException("No such user Exist"));

        Education education = new Education();
        education.setDegree(educationRequestDto.getDegree());
        education.setFieldOfStudy(educationRequestDto.getFieldOfStudy());
        education.setInstitute(educationRequestDto.getInstitute());
        education.setPassingYear(educationRequestDto.getPassingYear());

        userDetails.getEducations().add(education);

        userDetailRepository.save(userDetails);

        return new ApiResponse(true,"Education details added");
    }

    @Override
    public ApiResponse updateEducation(UUID authId, EducationRequestDto educationRequestDto) {
        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(()-> new UserNotFoundException("No such user Exist"));

        List<Education> education = userDetails.getEducations();
        Boolean flag = false;

        for (Education education1 : education){
            if (education1.getId().equals(educationRequestDto.getId())){
                flag = true;
                education1.setDegree(educationRequestDto.getDegree());
                education1.setFieldOfStudy(educationRequestDto.getFieldOfStudy());
                education1.setInstitute(educationRequestDto.getInstitute());
                education1.setPassingYear(educationRequestDto.getPassingYear());
            }
        }

        if (!flag){
            throw new ResourceNotFoundException("The education details with the provided id does not exist");
        }

        userDetailRepository.save(userDetails);

        return new ApiResponse(true,"Education details updated");
    }


}
