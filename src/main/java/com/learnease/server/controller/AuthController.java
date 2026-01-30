package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.InstructorRegisterRequestDto;
import com.learnease.server.dto.auth.LoginRequestDto;
import com.learnease.server.dto.auth.LoginResponseDto;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import com.learnease.server.dto.passwordreset.PasswordResetConfirmRequestDto;
import com.learnease.server.dto.passwordreset.PasswordResetOtpRequestDto;
import com.learnease.server.model.UserAuth;
import com.learnease.server.service.AuthService;
import com.learnease.server.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
@Tag(
        name = "Registration & Login APIs",
        description = "APIs for student , instructor registration and authentication"
)
public class AuthController {

    private final AuthService authService; // to call the service methods

    @Operation(summary = "Register a Student")
    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse> registerStudent(
           @Valid //this is for enabling the dto validations, first checks the dto validations here
           @RequestBody StudentRegisterRequestDto request
    ){
        ApiResponse response = authService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Register a Instructor")
    @PostMapping("/register/instructor")
    public ResponseEntity<?> registerInstructor(
            @Valid
            @RequestBody InstructorRegisterRequestDto request
    ){
        ApiResponse response = authService.registerInstructor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Sign in a User")
    @PostMapping("/signIn")
    public ResponseEntity<LoginResponseDto> signIn(@RequestBody @Valid LoginRequestDto requestDto){

        return ResponseEntity.status(200)
                .body(authService.login(requestDto));
    }


    @Operation(
            summary = "Request Password Reset OTP",
            description = "Generates OTP for password reset. Always returns success to avoid user enumeration."
    )
    @PostMapping("/password/reset/otp")
    public ResponseEntity<ApiResponse> requestPasswordResetOtp(@Valid @RequestBody PasswordResetOtpRequestDto request){
        ApiResponse response = authService.requestPasswordResetOtp(request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Reset Password using OTP",
            description = "Validates OTP and resets user password"
    )
    @PostMapping("/password/reset/confirm")
    public ResponseEntity<ApiResponse> resetPasswordWithOtp(
            @Valid @RequestBody PasswordResetConfirmRequestDto request
    ) {
        ApiResponse response = authService.resetPasswordWithOtp(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
        return ResponseEntity.ok(response);
    }

}
