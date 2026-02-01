package com.learnease.server.dto.admin;

import java.util.UUID;

public record CourseConversionDTO(
        UUID courseId,
        String title,
        Double conversionRate
) {}
