package com.learnease.server.service.impl;

import com.learnease.server.dto.CategoryRequestDto;
import com.learnease.server.exception.custom_exception.FileStorageException;
import com.learnease.server.model.Category;
import com.learnease.server.repository.CategoryRepository;
import com.learnease.server.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    @Override
    public Category registerCategory(CategoryRequestDto categoryRequest) {
        try {
            String imageUrl = s3Service.uploadFile(categoryRequest.getImage());

            Category category = new Category()
                    .setTitle(categoryRequest.getTitle())
                    .setDescription(categoryRequest.getDescription())
                    .setKeywords(categoryRequest.getKeywords())
                    .setCategoryImageUrl(imageUrl);

            return categoryRepository.save(category);
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage());
        }
    }

    @Override
    public Optional<Category> getCategoryByName(String categoryName) {
        return categoryRepository.findByTitle(categoryName);
    }

    @Override
    public List<Category> getCategoryByKeyword(String keyword) {
        return categoryRepository.findByKeyword(keyword);
    }


}
