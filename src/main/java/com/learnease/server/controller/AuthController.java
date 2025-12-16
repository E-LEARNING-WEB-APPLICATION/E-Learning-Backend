package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.auth.StudentRegisterRequestDto;
import com.learnease.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user/auth")
public class AuthController {

    @Autowired
    AuthService authService; // to call the service methods

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
