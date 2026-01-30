package com.learnease.server.model.enums;

public enum NotificationType {
//    SYSTEM,
//    USER,
//    INSTRUCTOR,
//    COURSE,
//    PAYMENT,
//    SUPPORT,
//    SECURITY


    // Admin
    INSTRUCTOR_APPROVAL_PENDING,

    // Instructor
    INSTRUCTOR_APPROVED,
    INSTRUCTOR_PAYOUT,
    COURSE_PUBLISHED,

    // Student
    COURSE_PURCHASED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,

    // System
    PASSWORD_CHANGED,
    SIGN_IN_OTP,
    EMAIL_VERIFICATION

}

