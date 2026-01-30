package com.learnease.server.model.enums;

public enum DashboardCourseSort {

    NONE(""),

    PRICE_LOW_HIGH("price_low_high"),
    PRICE_HIGH_LOW("price_high_low"),

    DISCOUNT_HIGH_LOW("discount_high_low"),

    NAME_ASC("name_asc"),
    NAME_DESC("name_desc"),

    RATING_HIGH_LOW("rating_high_low"),
    REVIEWS_HIGH_LOW("reviews_high_low");

    private final String value;

    DashboardCourseSort(String value) {
        this.value = value;
    }

    public static DashboardCourseSort from(String value) {
        for (DashboardCourseSort sort : values()) {
            if (sort.value.equalsIgnoreCase(value)) {
                return sort;
            }
        }
        return NONE;
    }
}
