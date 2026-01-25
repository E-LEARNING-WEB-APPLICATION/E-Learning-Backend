package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.Profile.*;
import com.learnease.server.exception.custom_exception.EmailAlreadyExistsException;
import com.learnease.server.exception.custom_exception.FileStorageException;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.*;
import com.learnease.server.repository.*;
import com.learnease.server.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final StudentRepository studentRepository;
    private final UserDetailRepository userDetailRepository;
    private final SkillsRepository skillsRepository;
    private final SpecializationRepository specializationRepository;
    private final UserAuthRepository userAuthRepository;
    private final S3Service s3Service;
    private final InstructorRepository instructorRepository;

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
        boolean flag = false;

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

    @Override
    public ApiResponse deleteEducation(UUID authId, UUID educationId) {

        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("No such user Exist"));

        List<Education> educations = userDetails.getEducations();

        boolean removed = educations.removeIf(
                edu -> edu.getId().equals(educationId)
        );

        if (!removed) {
            throw new ResourceNotFoundException(
                    "The education details with the provided id does not exist"
            );
        }

        userDetailRepository.save(userDetails);

        return new ApiResponse(true, "Education details deleted");
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillsRepository.findAll();
    }

    @Override
    public ApiResponse updateSkill(UUID authId, SkillRequestDto skillRequestDto) {

        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("No Such User Exist"));
        Student  student = studentRepository.findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("No Such Student Exist"));

        student.getSkills().clear();
        student.setSkills(skillRequestDto.getSkills());

        studentRepository.save(student);

        return new ApiResponse(true, "Skills details Updated");
    }

    @Override
    public ApiResponse updateProfile(UUID authId, StudentProfileRequestDto studentProfileRequestDto) {
        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("No Such User Exist"));
        if (userAuthRepository.existsByEmail(studentProfileRequestDto.getEmail())) {
            if(!userDetails.getUserAuth().getEmail().equals(studentProfileRequestDto.getEmail())){
                throw new EmailAlreadyExistsException("Email already registered");
            }
        }
        userDetails.setFirstName(studentProfileRequestDto.getFirstName());
        userDetails.setLastName(studentProfileRequestDto.getLastName());
        userDetails.setAddress(studentProfileRequestDto.getAddress());
        userDetails.getUserAuth().setEmail(studentProfileRequestDto.getEmail());
        userDetails.setPhoneNo(studentProfileRequestDto.getPhoneNo());
        userDetails.setDob(studentProfileRequestDto.getDob());
        userDetails.setGender(studentProfileRequestDto.getGender());

        return new ApiResponse(true, "Profile details Updated");
    }

    @Override
    public ApiResponse updateProfilePic(UUID authId, MultipartFile profilePic) {
        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("No Such User Exist"));

        String profilePicPath = null;

        try {
            profilePicPath = s3Service.uploadFile(profilePic,"ProfilePic");
        }catch (IOException e)
        {
            throw new FileStorageException("Error While Uploading Image");
        }

        userDetails.setProfilePic(profilePicPath);
        userDetailRepository.save(userDetails);
        return new ApiResponse(true, "Profile Pic Updated");
    }

    @Override
    public InstructorProfileResponseDto getInstructorDetails(UUID authId) {

        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("No Such User Exist"));
        Instructor instructor = instructorRepository.findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("No Such Student Exist"));
        InstructorProfileResponseDto instructorProfileResponseDto = new InstructorProfileResponseDto();

        instructorProfileResponseDto.setFirstName(userDetails.getFirstName());
        instructorProfileResponseDto.setLastName(userDetails.getLastName());
        instructorProfileResponseDto.setAddress(userDetails.getAddress());
        instructorProfileResponseDto.setEmail(userDetails.getUserAuth().getEmail());
        instructorProfileResponseDto.setPhoneNo(userDetails.getPhoneNo());
        instructorProfileResponseDto.setDob(userDetails.getDob());
        instructorProfileResponseDto.setGender(userDetails.getGender());
        instructorProfileResponseDto.setEducations(userDetails.getEducations());
        instructorProfileResponseDto.setProfilePic(userDetails.getProfilePic());
        instructorProfileResponseDto.setSpecializations(instructor.getSpecializations());
        instructorProfileResponseDto.setBio(instructor.getBio());
        instructorProfileResponseDto.setExperience(instructor.getExperience());
        instructorProfileResponseDto.setGitHubUrl(instructor.getGitHubUrl());
        instructorProfileResponseDto.setLinkedInUrl(instructor.getLinkedInUrl());
        instructorProfileResponseDto.setTwitterUrl(instructor.getTwitterUrl());

        return instructorProfileResponseDto;

    }

    @Override
    public List<Specialization> getAllSpecialization() {
        return specializationRepository.findAll();
    }

    @Override
    public ApiResponse updateSpecialization(UUID authId, SpecializationRequestDto specializationRequestDto) {
        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("No Such User Exist"));
        Instructor  instructor = instructorRepository.findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("No Such Student Exist"));

        instructor.getSpecializations().clear();
        instructor.setSpecializations(specializationRequestDto.getSpecializations());

        instructorRepository.save(instructor);

        return new ApiResponse(true, "Specialization details Updated");
    }

}
