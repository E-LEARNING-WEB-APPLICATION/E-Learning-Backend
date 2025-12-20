package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import com.learnease.server.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService; // to call the service methods

    @Operation(summary = "Register a Student")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerStudent(
           @Valid //this is for enabling the dto validations, first checks the dto validations here
           @RequestBody StudentRegisterRequestDto request
    ){
        ApiResponse response = authService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
