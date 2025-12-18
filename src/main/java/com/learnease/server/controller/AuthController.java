package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import com.learnease.server.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService; // to call the service methods

    @Operation(summary = "Register a Student")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> studentRegistrationController(@RequestBody StudentRegisterRequestDto request){
        ApiResponse response = authService.studentRegistrationService(request);
        if(response.isSuccess()){
            return new ResponseEntity<>(response , HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
        }
    }
}
