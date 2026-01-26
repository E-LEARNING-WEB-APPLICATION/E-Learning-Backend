package com.learnease.server.projection.course;

import java.util.UUID;

public interface DashboardCoursesProjection {

    UUID getId();
    UUID getCategoryId();

    String getThumbnail();
    String getTitle();

    Double getFees();
    Double getRating();

    Long getReviews();

    Integer getDuration();
    Integer getDiscount();
}
