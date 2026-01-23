package com.learnease.server.service.impl;

import com.learnease.server.dto.CategoryRequestDto;
import com.learnease.server.dto.CategoryUpdateRequestDto;
import com.learnease.server.exception.custom_exception.BadClientRequestException;
import com.learnease.server.exception.custom_exception.FileStorageException;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.model.Category;
import com.learnease.server.repository.CategoryRepository;
import com.learnease.server.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    @Override
    public Category registerCategory(CategoryRequestDto categoryRequest) {
        try {
            String imageUrl = s3Service.uploadFile(categoryRequest.getImage(),"");

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

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(UUID id, CategoryUpdateRequestDto req) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Category not found for given Id"));
            if (req.getTitle() != null) {
                category.setTitle(req.getTitle());
            }

            if (req.getDescription() != null) {
                category.setDescription(req.getDescription());
            }

            if (req.getKeywords() != null) {
                category.setKeywords(
                        req.getKeywords().stream()
                                .map(String::toLowerCase)
                                .map(String::trim)
                                .collect(Collectors.toSet())
                );
            }

            if (req.getImage() != null && !req.getImage().isEmpty()) {
                String imageUrl = s3Service.uploadFile(req.getImage(),"");
                category.setCategoryImageUrl(imageUrl);
            }

            return categoryRepository.save(category);
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage());
        }
    }


}
