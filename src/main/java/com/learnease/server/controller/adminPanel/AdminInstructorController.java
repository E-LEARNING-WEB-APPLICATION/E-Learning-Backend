package com.learnease.server.controller.adminPanel;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.InstructorResponseDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.admin.InstructorLeaderboardDTO;
import com.learnease.server.model.enums.Status;
import com.learnease.server.service.AdminService;
import com.learnease.server.service.InstructorStatisticsService;
import com.learnease.server.util.enums.InstructorSortBy;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/instructors")
@RequiredArgsConstructor
public class AdminInstructorController {
    private final AdminService adminService;
    private final InstructorStatisticsService instructorStatisticsService;

    @Operation(summary =  "get all instructors by status, add status param PENDING to get pending instructors")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InstructorResponseDto>> getInstructorsByStatus(
            @RequestParam(name = "status", required = false) Status status
    ) {
        List<InstructorResponseDto> instructors = adminService.getInstructorByStatus(status);
        return ResponseEntity.ok(instructors);
    }

    @Operation(summary =  "get all instructor count by status, add status param PENDING to get pending instructors count")
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getInstructorsCountByStatus(
            @RequestParam(name = "status", required = false) Status status
    ) {
        long instructorCount = adminService.getInstructorCountByStatus(status);
        return ResponseEntity.ok(new ApiResponse<>(true, instructorCount));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveInstructor(
            @AuthenticationPrincipal JWTDTO admin,
            @PathVariable UUID id) {

        adminService.approveInstructor(admin.getUserId(), id);
        return ResponseEntity.ok(new ApiResponse(true, "Instructor approved"));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectInstructor(
            @AuthenticationPrincipal JWTDTO admin,
            @PathVariable UUID id) {

        adminService.rejectInstructor(admin.getUserId(), id);
        return ResponseEntity.ok(new ApiResponse(true, "Instructor approved"));
    }

    @GetMapping("/leaderboard")
    public List<InstructorLeaderboardDTO> leaderboard(
            @RequestParam InstructorSortBy sortBy,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return instructorStatisticsService.getTopInstructors(sortBy, limit);
    }

    @PutMapping("/UpdateCommission")
    public ResponseEntity<?> updateCommission(@RequestParam Double commission, Authentication authentication){

        JWTDTO jwtdto = (JWTDTO) authentication.getPrincipal();
        UUID authId = jwtdto.getUserId();
        ApiResponse response = adminService.addOrUpdateCommission(commission,authId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
