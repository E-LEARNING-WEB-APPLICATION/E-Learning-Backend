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
    COURSE_SUBMITTED_FOR_REVIEW,
    COURSE_REJECTED,
    USER_REPORTED,

    // Instructor
    COURSE_APPROVED,
    COURSE_REJECTED_BY_ADMIN,
    STUDENT_ENROLLED,
    COURSE_PUBLISHED,

    // Student
    COURSE_ENROLLED,
    COURSE_COMPLETED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,

    // System
    PASSWORD_CHANGED,
    ACCOUNT_SUSPENDED

}

