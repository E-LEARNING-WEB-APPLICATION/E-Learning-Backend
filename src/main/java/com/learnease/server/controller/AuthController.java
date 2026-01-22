package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.InstructorRegisterRequestDto;
import com.learnease.server.dto.auth.LoginRequestDto;
import com.learnease.server.dto.auth.LoginResponseDto;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
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


@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
@Tag(
        name = "Registration & Login APIs",
        description = "APIs for student , instructor registration and authentication"
)
public class AuthController {

    private final AuthService authService; // to call the service methods
    private final AuthenticationManager authenticationManager; //for the Managers authenticate method
    private final JwtUtil jwtUtil;

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
    public ResponseEntity<?> signIn(@RequestBody @Valid LoginRequestDto requestDto){
        /*
            1.Invoke AuthenticationManager's authenticate method
            public Authentication authenticate(Authentication auth)
            Failure - throws AuthenticationException

            Authenticcation - i/f
            Implemented by class -
            UserNamePasswordAuthenticationToken(Object email , Object password)
         */

        System.out.println("Inside Login");

        Authentication fullyAuthenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail() , requestDto.getPassword()
                ));
        String token = jwtUtil.generateToken((UserAuth) fullyAuthenticated.getPrincipal());
        System.out.println(token);
        return ResponseEntity.status(200)
                .body(new LoginResponseDto( true ,"Login Successful" , token));
    }
}
