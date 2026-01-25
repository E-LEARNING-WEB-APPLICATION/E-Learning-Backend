package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.profile.*;
import com.learnease.server.model.Skill;
import com.learnease.server.model.Specialization;
import com.learnease.server.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
    @PostMapping("/addEducation")
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

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get All the skills for selection")
    @GetMapping("/getAllSkill")
    public ResponseEntity<?> getAllSkills(){
        List<Skill> skills = profileService.getAllSkills();
        return ResponseEntity.status(HttpStatus.FOUND).body(skills);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update the skills from the profile")
    @PutMapping("/updateSkill")
    public ResponseEntity<?> updateUserSkill(@RequestBody SkillRequestDto skillRequestDto, Authentication authentication){
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();

        ApiResponse response = profileService.updateSkill(authId,skillRequestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update the Student profile info")
    @PutMapping("/updateStudentProfile")
    public ResponseEntity<?> updateStudentProfileInfo(@RequestBody @Valid StudentProfileRequestDto studentProfileRequestDto,Authentication authentication){
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();

        ApiResponse response = profileService.updateStudentProfile(authId,studentProfileRequestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update the profile Pic")
    @PutMapping(value = "/updateProfilePic",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfilePic(@RequestParam MultipartFile profilePic,Authentication authentication){
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();

        ApiResponse response = profileService.updateProfilePic(authId,profilePic);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get the Profile details for Instructor")
    @GetMapping("/instructor")
    public ResponseEntity<?> getInstructorDetails(Authentication authentication){
        //to get the user id from the jwt token
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();
        InstructorProfileResponseDto instructorProfileResponseDto = profileService.getInstructorDetails(authId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(instructorProfileResponseDto);
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get All the Specialization for selection")
    @GetMapping("/getAllSpecialization")
    public ResponseEntity<?> getAllSpecialization(){
        List<Specialization> specializationSet = profileService.getAllSpecialization();
        return ResponseEntity.status(HttpStatus.FOUND).body(specializationSet);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update the Specialization from the profile")
    @PutMapping("/updateSpecialization")
    public ResponseEntity<?> updateUserSpecialization(@RequestBody SpecializationRequestDto specializationRequestDto, Authentication authentication){
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();

        ApiResponse response = profileService.updateSpecialization(authId,specializationRequestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update the Instructor profile info")
    @PutMapping("/updateInstructorProfile")
    public ResponseEntity<?> updateInstructorProfileInfo(@RequestBody @Valid InstructorProfileRequestDto instructorProfileRequestDto,Authentication authentication){
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwt.getUserId();

        ApiResponse response = profileService.updateInstructorProfile(authId,instructorProfileRequestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
