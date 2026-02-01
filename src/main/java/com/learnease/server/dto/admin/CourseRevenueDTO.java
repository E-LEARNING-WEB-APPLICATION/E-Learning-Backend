package com.learnease.server.dto.admin;

import java.math.BigDecimal;
import java.util.UUID;

public record CourseRevenueDTO (
        UUID id,
        String title,
        BigDecimal revenue
){}
