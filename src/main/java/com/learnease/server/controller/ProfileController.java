package com.learnease.server.controller;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.student.ProfileDto;
import com.learnease.server.model.UserAuth;
import com.learnease.server.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Get the Profile details")
    @GetMapping("/student")
    public ResponseEntity<?> getStudentDetails(Authentication authentication){
        //to get the user id from the jwt token
        JWTDTO jwt = (JWTDTO) authentication.getPrincipal();

        Long userId = jwt.getUserId();

        ProfileDto profileDto = profileService.getStudentDetails(userId);

        return ResponseEntity.ok(profileDto);
    }
}
