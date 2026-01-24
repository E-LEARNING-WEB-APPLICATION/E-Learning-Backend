package com.learnease.server.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoursesDto {

        private String title;
        private String description;
        private double fees;
        private int discount;
        private String thumbnail;
        private String introVideo;
        private int hour;

}
