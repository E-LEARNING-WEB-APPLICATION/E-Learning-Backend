package com.learnease.server.service;

import com.learnease.server.dto.CategoryRequestDto;
import com.learnease.server.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public Category registerCategory(CategoryRequestDto categoryRequest);
    public Optional<Category> getCategoryByName(String categoryName);
    public List<Category> getCategoryByKeyword(String keyword);
}
