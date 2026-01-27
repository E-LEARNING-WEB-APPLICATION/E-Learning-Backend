package com.learnease.server.service;

import com.learnease.server.dto.category.CategoryRequestDto;
import com.learnease.server.dto.category.CategoryResponseDto;
import com.learnease.server.dto.category.CategoryUpdateRequestDto;
import com.learnease.server.model.Category;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    public Category registerCategory(CategoryRequestDto categoryRequest);
    public Optional<Category> getCategoryByName(String categoryName);
    public List<Category> getCategoryByKeyword(String keyword);
    public List<CategoryResponseDto> getAllCategories();
    long getAllCategoryCount();

    Category updateCategory(UUID id, @Valid CategoryUpdateRequestDto request);
}
