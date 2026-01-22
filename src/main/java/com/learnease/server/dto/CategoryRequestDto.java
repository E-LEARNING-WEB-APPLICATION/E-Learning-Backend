package com.learnease.server.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class CategoryRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Set<String> keywords;

    @NotNull
    private MultipartFile image;

    @AssertTrue
    public boolean isImage() {
        return image != null && image.getContentType().startsWith("image/");
    }
}
