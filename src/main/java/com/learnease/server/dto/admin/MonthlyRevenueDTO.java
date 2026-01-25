package com.learnease.server.dto.admin;

import java.math.BigDecimal;


public record MonthlyRevenueDTO(
        int year,
        int month,
        BigDecimal revenue
) {}