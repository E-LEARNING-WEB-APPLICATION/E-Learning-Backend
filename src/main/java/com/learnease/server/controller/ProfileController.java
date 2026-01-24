package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.Profile.EducationRequestDto;
import com.learnease.server.dto.Profile.StudentProfileResponseDto;
import com.learnease.server.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Tag(
        name = "Profile api for student and instructor",
        description = "Profile get and update along with education and skills get and update"
)
public class ProfileController {

    private final ProfileService profileService;


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get the Profile details")
    @GetMapping("/student")
    public ResponseEntity<?> getStudentDetails(Authentication authentication){
        //to get the user id from the jwt token
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();
        StudentProfileResponseDto studentProfileResponseDto = profileService.getStudentDetails(authId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentProfileResponseDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Post Education details")
    @PostMapping("/addEducaion")
    public ResponseEntity<?> addEducationDetails(@RequestBody @Valid EducationRequestDto educationRequestDto , Authentication authentication){

        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();

        ApiResponse response = profileService.addEducation(authId,educationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Update Education Details")
    @PutMapping("/updateEducation")
    public ResponseEntity<?> updateEducationDetails(@RequestBody @Valid EducationRequestDto educationRequestDto, Authentication authentication){
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();

        ApiResponse response = profileService.updateEducation(authId,educationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Delete Education Details")
    @DeleteMapping("/deleteEducation/{educationId}")
    public ResponseEntity<?> deleteEducationDetails(@PathVariable UUID educationId,Authentication authentication){

        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();

        ApiResponse response = profileService.deleteEducation(authId,educationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
