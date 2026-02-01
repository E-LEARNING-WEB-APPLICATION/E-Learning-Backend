package com.learnease.server.dto.admin;

import java.util.UUID;

public record InstructorSummaryDTO(
        UUID instructorId,
        String instructorName,
        String email
) {
}
