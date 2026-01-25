package com.learnease.server.dto.admin;

import com.learnease.server.model.Instructor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class InstructorEnrollmentsDTO {
    private UUID instructorId;
    private String instructorName;
    private long enrollments;
}
