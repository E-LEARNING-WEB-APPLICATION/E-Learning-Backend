package com.learnease.server.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTopicRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private int hour;
    private int min;

    @NotNull
    private MultipartFile video;

    @NotBlank
    private String notes;
}

