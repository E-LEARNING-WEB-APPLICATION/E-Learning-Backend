package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import com.learnease.server.model.Student;
import com.learnease.server.model.UserAuth;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.Role;
import com.learnease.server.model.enums.Status;
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
    private final StudentRepository studentRepository; //used to save the entity to the db
    private final PasswordEncoder passwordEncoder; //to encode the password

    @Override
    public ApiResponse studentRegistrationService(StudentRegisterRequestDto requestDto) {

        if(requestDto == null){
            return new ApiResponse(false , "Request body can-not be null");
        };

        if(requestDto.getEmail() == null || requestDto.getEmail().isBlank()){
            return new ApiResponse(false , "Email can-not be empty");
        };

        if(requestDto.getPassword() == null || requestDto.getPassword().isBlank()){
            return new ApiResponse(false , "Password can-not be empty");
        };

        if(requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()){
            return new ApiResponse(false , "First Name can-not be empty");
        };

        if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()) {
            return new ApiResponse(false, "Last name is required");
        };

        if(requestDto.getPhoneNo() == null || requestDto.getPhoneNo().isBlank()){
            return new ApiResponse(false, "Phone Number is required");
        };

        if(userAuthRepository.existsByEmail(requestDto.getEmail())){
            return new ApiResponse(false , "Email Already Registered");
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
}
