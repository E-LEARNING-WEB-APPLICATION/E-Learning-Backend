package com.learnease.server.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddSectionRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
