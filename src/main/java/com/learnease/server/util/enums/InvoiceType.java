package com.learnease.server.util.enums;

public enum InvoiceType {
    COURSE_PURCHASE("invoice/course-purchase"),
    INSTRUCTOR_PAYOUT("invoice/instructor-payout"),
    REFUND("invoice/refund");

    private final String templatePath;

    InvoiceType(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplatePath() {
        return templatePath;
    }
}
