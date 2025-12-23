package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.InstructorRegisterRequestDto;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import com.learnease.server.exception.custom_exception.EmailAlreadyExistsException;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.Student;
import com.learnease.server.model.UserAuth;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
import com.learnease.server.repository.InstructorRepository;
import com.learnease.server.repository.StudentRepository;
import com.learnease.server.repository.UserAuthRepository;
import com.learnease.server.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserAuthRepository userAuthRepository; //using this for email check(already exist)
    private final StudentRepository studentRepository; //used to save the student entity to the db
    private final InstructorRepository instructorRepository; //used to save the instructor entity to the db
    private final PasswordEncoder passwordEncoder; //to encode the password


    @Override
    public ApiResponse registerStudent(StudentRegisterRequestDto requestDto) {

        if(userAuthRepository.existsByEmail(requestDto.getEmail())){
            throw new EmailAlreadyExistsException("Email already registered");
        };

        UserAuth userAuth = new UserAuth();
        userAuth.setEmail(requestDto.getEmail());
        userAuth.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userAuth.setRole(Role.STUDENT);
        userAuth.setStatus(Status.ACTIVE);

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(requestDto.getFirstName());
        userDetails.setLastName(requestDto.getLastName());
        userDetails.setPhoneNo(requestDto.getPhoneNo());
        userDetails.setUserAuth(userAuth);

        Student student = new Student();
        student.setUserDetails(userDetails);
        student.setTotalEnrolledCourses(0);

        studentRepository.save(student);

        return new ApiResponse(true , "Student Registered Successfully.");
    }

    @Override
    public ApiResponse registerInstructor(InstructorRegisterRequestDto request) {
        if(userAuthRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already registered");
        }

        UserAuth userAuth = new UserAuth();
        userAuth.setEmail(request.getEmail());
        userAuth.setPassword(passwordEncoder.encode(request.getPassword()));
        userAuth.setRole(Role.INSTRUCTOR);
        userAuth.setStatus(Status.PENDING);

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(request.getFirstName());
        userDetails.setLastName(request.getLastName());
        userDetails.setPhoneNo(request.getPhoneNo());
        userDetails.setUserAuth(userAuth);

        Instructor instructor = new Instructor();
        instructor.setBio(request.getBio());
        instructor.setExperience(request.getExperience());
        instructor.setUserDetails(userDetails);

        instructorRepository.save(instructor);

        return new ApiResponse(true , "Instructor Registered Successfully.");
    }
}
