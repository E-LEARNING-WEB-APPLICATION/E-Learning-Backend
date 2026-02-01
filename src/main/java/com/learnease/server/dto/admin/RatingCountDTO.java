package com.learnease.server.dto.admin;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record RatingCountDTO(
        @JsonIgnore Double rating,
        Long count
) {
    @JsonGetter("rating")
    public int getRatingInt() {
        return rating != null ? rating.intValue() : 0;
    }
}
