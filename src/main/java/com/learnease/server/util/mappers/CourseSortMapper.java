package com.learnease.server.util.mappers;

import com.learnease.server.util.enums.CourseSortField;
import org.springframework.data.domain.Sort;

public class CourseSortMapper {

    public static Sort map(CourseSortField field, Sort.Direction direction) {
        return switch (field) {
            case TOTAL_ENROLLMENTS -> Sort.by(direction, "totalEnrollments");
            case TOTAL_REVENUE -> Sort.by(direction, "totalRevenue");
            case AVG_RATING -> Sort.by(direction, "avgRating");
            case RECENT_ENROLLMENTS -> Sort.by(direction, "recentEnrollments");
            case CREATED_AT -> Sort.by(direction, "createdAt");
        };
    }
}

