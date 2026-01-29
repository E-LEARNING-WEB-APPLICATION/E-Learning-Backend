package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.InstructorRegisterRequestDto;
import com.learnease.server.dto.auth.LoginRequestDto;
import com.learnease.server.dto.auth.LoginResponseDto;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import com.learnease.server.dto.notification.EmailEvent;
import com.learnease.server.dto.notification.SendNotificationDTO;
import com.learnease.server.exception.custom_exception.EmailAlreadyExistsException;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.Student;
import com.learnease.server.model.UserAuth;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.*;
import com.learnease.server.repository.AdminRepository;
import com.learnease.server.repository.InstructorRepository;
import com.learnease.server.repository.StudentRepository;
import com.learnease.server.repository.UserAuthRepository;
import com.learnease.server.service.AuthService;
import com.learnease.server.service.EmailService;
import com.learnease.server.service.NotificationService;
import com.learnease.server.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserAuthRepository userAuthRepository; //using this for email check(already exist)
    private final StudentRepository studentRepository; //used to save the student entity to the db
    private final InstructorRepository instructorRepository; //used to save the instructor entity to the db
    private final PasswordEncoder passwordEncoder; //to encode the password
    private final NotificationService notificationService;
    private final AuthenticationManager authenticationManager; //for the Managers authenticate method
    private final JwtUtil jwtUtil;
    private final EmailService emailService;


    @Override
    public ApiResponse<String> registerStudent(StudentRegisterRequestDto requestDto) {

        if (userAuthRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        ;

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

        return new ApiResponse<>(true, "Student Registered Successfully.");
    }

    @Override
    public ApiResponse<String> registerInstructor(InstructorRegisterRequestDto request) {
        if (userAuthRepository.existsByEmail(request.getEmail())) {
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

        notificationService.sendNotification(
                SendNotificationDTO.builder()
                        .title("New Instructor Registered")
                        .message(instructor.getUserDetails().getFirstName()
                                + instructor.getUserDetails().getLastName()
                                + " registered as instructor. Waiting for approval")
                        .type(NotificationType.INSTRUCTOR_APPROVAL_PENDING)
                        .priority(NotificationPriority.MEDIUM)
                        .subjectId(instructor.getId())
                        .subjectType(NotificationSubjectType.INSTRUCTOR)
                        .role(Role.ADMIN)
                        .build()
        );

        //send Email to admin
        emailService.sendEmail(
                EmailEvent.builder()
                        .eventType(NotificationType.INSTRUCTOR_APPROVAL_PENDING)
//                        .to(userAuthRepository.findEmailByRoleAndStatusActive(Role.ADMIN, Status.ACTIVE))
                        .to(List.of("gandhioms16@gmail.com"))
                        .subject("New Instructor Registered")
                        .data(Map.of(
                                "firstName",
                                instructor.getUserDetails().getFirstName(),
                                "lastName", instructor.getUserDetails().getLastName(),
                                "email", instructor.getUserDetails().getUserAuth().getEmail(),
                                "phoneNo", instructor.getUserDetails().getPhoneNo(),
                                "experience", instructor.getExperience()
                        ))
                        .meta(Map.of())
                        .build()
        );


        return new ApiResponse<>(true, "Instructor Registered Successfully.");
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
              /*
            1.Invoke AuthenticationManager's authenticate method
            public Authentication authenticate(Authentication auth)
            Failure - throws AuthenticationException

            Authenticcation - i/f
            Implemented by class -
            UserNamePasswordAuthenticationToken(Object email , Object password)
         */

        Authentication fullyAuthenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(), requestDto.getPassword()
                ));
        UserAuth userAuth = (UserAuth) fullyAuthenticated.getPrincipal();
        userAuth.setLastLoginAt(LocalDateTime.now());
        String token = jwtUtil.generateToken((UserAuth) fullyAuthenticated.getPrincipal());
        return new LoginResponseDto(true, "Login Successful", token);
    }
}
