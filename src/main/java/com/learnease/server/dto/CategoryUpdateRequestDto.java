package com.learnease.server.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class CategoryUpdateRequestDto {

    @Size(min = 2, max = 100)
    private String title;

    @Size(max = 300)
    private String description;

    private Set<String> keywords;

    // Optional image update
    private MultipartFile image;
}

