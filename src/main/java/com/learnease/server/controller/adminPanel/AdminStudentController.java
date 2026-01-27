package com.learnease.server.controller.adminPanel;

import com.learnease.server.dto.admin.MonthlyStudentEnrollmentDTO;
import com.learnease.server.service.AdminService;
import com.learnease.server.service.AdminStatisticsService;
import com.learnease.server.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/student")
@RequiredArgsConstructor
public class AdminStudentController {
    private final StudentService studentService;
    private final AdminService adminService;
    private final AdminStatisticsService statisticsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count")
    public ResponseEntity<Long> getAllStudentCount(){
        return ResponseEntity.ok(studentService.getCountOfStudents());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/enrolled/count")
    public ResponseEntity<?> getCountOfEnrolledStudent(@RequestParam String duration){
        switch (duration){
            case "1d":
                return ResponseEntity.ok(studentService.getEnrolledStudentCountAfterDate(LocalDateTime.now().minusDays(1)));
            case "1w":
                return ResponseEntity.ok(studentService.getEnrolledStudentCountAfterDate(LocalDateTime.now().minusDays(7)));
            case "1m":
                return ResponseEntity.ok(studentService.getEnrolledStudentCountAfterDate(LocalDateTime.now().minusDays(30)));
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("duration invalid");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active/count")
    public ResponseEntity<?> getCountOfActiveStudent(@RequestParam String duration){
        switch (duration){
            case "1d":
                return ResponseEntity.ok(adminService.getActiveStudentCountByDate(LocalDateTime.now().minusDays(1)));
            case "1w":
                return ResponseEntity.ok(adminService.getActiveStudentCountByDate(LocalDateTime.now().minusDays(7)));
            case "1m":
                return ResponseEntity.ok(adminService.getActiveStudentCountByDate(LocalDateTime.now().minusDays(30)));
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("duration invalid");
        }
    }

    @GetMapping("/enrolled/monthly")
    public ResponseEntity<List<MonthlyStudentEnrollmentDTO>> getStudentEnrollments(
            @RequestParam(defaultValue = "6") int months
    ) {
        return ResponseEntity.ok(
                statisticsService.getMonthlyStudentEnrollments(months)
        );
    }

}
