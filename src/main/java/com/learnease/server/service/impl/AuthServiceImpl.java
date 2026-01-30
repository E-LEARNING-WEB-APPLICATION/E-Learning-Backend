package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.InstructorRegisterRequestDto;
import com.learnease.server.dto.auth.LoginRequestDto;
import com.learnease.server.dto.auth.LoginResponseDto;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import com.learnease.server.dto.notification.EmailEvent;
import com.learnease.server.dto.notification.SendNotificationDTO;
import com.learnease.server.exception.custom_exception.EmailAlreadyExistsException;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.Student;
import com.learnease.server.model.UserAuth;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.*;
import com.learnease.server.repository.*;
import com.learnease.server.service.AuthService;
import com.learnease.server.service.EmailService;
import com.learnease.server.service.NotificationService;
import com.learnease.server.service.OtpService;
import com.learnease.server.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final OtpService otpService;
    private final UserDetailRepository userDetailRepository;


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
        userAuth.setStatus(Status.PENDING);
        userAuth.setEmailVerified(false);

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(requestDto.getFirstName());
        userDetails.setLastName(requestDto.getLastName());
        userDetails.setPhoneNo(requestDto.getPhoneNo());
        userDetails.setUserAuth(userAuth);

        Student student = new Student();
        student.setUserDetails(userDetails);
        student.setTotalEnrolledCourses(0);

        studentRepository.save(student);

        String otp = otpService.generateAndSaveOtp(
                userAuth.getId(),
                OtpPurpose.EMAIL_VERIFY
        );

        emailService.sendEmail(
                EmailEvent.builder()
                        .eventType(NotificationType.EMAIL_VERIFICATION)
                        .to(List.of(userAuth.getEmail()))
                        .subject("Verify your email")
                        .data(Map.of(
                                "otp", otp,
                                "validForMinutes", 5
                        ))
                        .meta(Map.of())
                        .build()
        );

        return new ApiResponse<>(
                true,
                "Registration started. Please verify your email."
        );
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
        userAuth.setEmailVerified(false);

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

        String otp = otpService.generateAndSaveOtp(
                userAuth.getId(),
                OtpPurpose.EMAIL_VERIFY
        );

        // Send verification email to instructor
        emailService.sendEmail(
                EmailEvent.builder()
                        .eventType(NotificationType.EMAIL_VERIFICATION)
                        .to(List.of(userAuth.getEmail()))
                        .subject("Verify your email")
                        .data(Map.of(
                                "otp", otp,
                                "validForMinutes", 5
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

        // Block login if email not verified
        if (!userAuth.isEmailVerified()) {
            throw new EmailAlreadyExistsException("Please verify your email before logging in");
        }

        userAuth.setLastLoginAt(LocalDateTime.now());
        String token = jwtUtil.generateToken((UserAuth) fullyAuthenticated.getPrincipal());
        return new LoginResponseDto(true, "Login Successful", token);
    }

    @Override
    public ApiResponse<String> requestPasswordResetOtp(String email) {

        userAuthRepository.findByEmail(email).ifPresent(user -> {

            String otp = otpService.generateAndSaveOtp(
                    user.getId(),
                    OtpPurpose.PASSWORD_RESET
            );

            emailService.sendEmail(
                    EmailEvent.builder()
                            .eventType(NotificationType.PASSWORD_CHANGED)
                            .to(List.of(user.getEmail()))
                            .subject("Reset Your Password")
                            .data(Map.of(
                                    "userName", email,
                                    "otp", otp,
                                    "validForMinutes", 5
                            ))
                            .meta(Map.of(
                                    "channel", "EMAIL",
                                    "priority", "HIGH"
                            ))
                            .build()
            );
        });


        return new ApiResponse<>(
                true,
                "If the email exists, OTP has been sent"
        );
    }

    @Override
    public ApiResponse<String> resetPasswordWithOtp(String email, String otp, String newPassword) {

        UserAuth user = userAuthRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("Invalid Email"));

        otpService.validateOtp(user.getId() , OtpPurpose.PASSWORD_RESET , otp);

        user.setPassword(passwordEncoder.encode(newPassword));
        userAuthRepository.save(user);

        otpService.consumeOtp(user.getId() , OtpPurpose.PASSWORD_RESET);

        return new ApiResponse<>(true , "Password reset successful");
    }

    @Override
    public ApiResponse<String> sendEmailVerificationOtp(String email) {

        UserAuth userAuth = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Invalid email"));

        // If already verified, block resend
        if (userAuth.isEmailVerified()) {
            return new ApiResponse<>(
                    true,
                    "Email already verified"
            );
        };

        String otp = otpService.generateAndSaveOtp(
                userAuth.getId(),
                OtpPurpose.EMAIL_VERIFY
        );

        emailService.sendEmail(
                EmailEvent.builder()
                        .eventType(NotificationType.EMAIL_VERIFICATION)
                        .to(List.of(userAuth.getEmail()))
                        .subject("Verify your email")
                        .data(Map.of(
                                "otp", otp,
                                "validForMinutes", 5
                        ))
                        .meta(Map.of())
                        .build()
        );

        return new ApiResponse<>(
                true,
                "Verification OTP sent to email"
        );
    }


    @Override
    public ApiResponse verifyEmailOtp(String email, String otp) {

        UserAuth userAuth = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Invalid email"));

        otpService.validateOtp(
                userAuth.getId(),
                OtpPurpose.EMAIL_VERIFY,
                otp
        );

        userAuth.setEmailVerified(true);
        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(userAuth.getId()).orElseThrow(()->new UserNotFoundException("User Not Found"));

        if (userAuth.getRole() == Role.STUDENT) {
            userAuth.setStatus(Status.ACTIVE);
                       emailService.sendEmail(
                        EmailEvent.builder()
                            .eventType(NotificationType.STUDENT_REGISTERED)
                            .to(List.of(userAuth.getEmail()))
                            .subject("LearnEase: Registration Successful!")
                            .data(Map.of(
                                    "firstName", userDetails.getFirstName(),
                                    "email", userAuth.getEmail(),
                                    "platformName", "LearnEase",
                                    "supportEmail", "support@learnease.com",
                                    "year", LocalDate.now().getYear()
                            ))
                            .meta(Map.of())
                            .build()
            );
        }

        userAuthRepository.save(userAuth);

        otpService.consumeOtp(
                userAuth.getId(),
                OtpPurpose.EMAIL_VERIFY
        );

        if (userAuth.getRole() == Role.INSTRUCTOR) {
            emailService.sendEmail(
                    EmailEvent.builder()
                            .eventType(NotificationType.STUDENT_REGISTERED)
                            .to(List.of(userAuth.getEmail()))
                            .subject("LearnEase: Registration Successful!")
                            .data(Map.of(
                                    "instructorName", userDetails.getFirstName(),
                                    "platformName", "LearnEase",
                                    "supportEmail", "support@learnease.com",
                                    "year", LocalDate.now().getYear()
                            ))
                            .meta(Map.of())
                            .build()
            );

            Instructor instructor = instructorRepository
                    .findByUserDetails_UserAuth_Id(userAuth.getId())
                    .orElseThrow(() ->
                            new IllegalStateException("Instructor not found for verified user"));

            notificationService.sendNotification(
                    SendNotificationDTO.builder()
                            .title("Instructor Email Verified")
                            .message(
                                    instructor.getUserDetails().getFirstName() + " " +
                                            instructor.getUserDetails().getLastName() +
                                            " verified email and is waiting for approval"
                            )
                            .type(NotificationType.INSTRUCTOR_APPROVAL_PENDING)
                            .priority(NotificationPriority.MEDIUM)
                            .subjectId(instructor.getId())
                            .subjectType(NotificationSubjectType.INSTRUCTOR)
                            .role(Role.ADMIN)
                            .build()
            );

            emailService.sendEmail(
                    EmailEvent.builder()
                            .eventType(NotificationType.INSTRUCTOR_APPROVAL_PENDING)
                            .to(userAuthRepository.findEmailByRoleAndStatus(Role.ADMIN, Status.ACTIVE))
                            .subject("Instructor Waiting for Approval")
                            .data(Map.of(
                                    "firstName", instructor.getUserDetails().getFirstName(),
                                    "lastName", instructor.getUserDetails().getLastName(),
                                    "email", instructor.getUserDetails().getUserAuth().getEmail(),
                                    "phoneNo", instructor.getUserDetails().getPhoneNo(),
                                    "experience", instructor.getExperience()
                            ))
                            .meta(Map.of())
                            .build()
            );
        }

        // Final response
        return new ApiResponse<>(
                true,
                "Email verified successfully."
        );
    };
};
