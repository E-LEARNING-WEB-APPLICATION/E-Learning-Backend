package com.learnease.server.controller;

import com.learnease.server.dto.category.CategoryRequestDto;
import com.learnease.server.dto.category.CategoryUpdateRequestDto;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.model.Category;
import com.learnease.server.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "create new Category")
    @PreAuthorize("hasRole('ADMIN')")
    /*
     * consumes = MediaType.MULTIPART_FORM_DATA_VALUE is mandatory
     * because:
     * - Client sends a form (React FormData)
     * - Request contains a file upload
     * - Without this, Spring may reject the request with 415 error
     */
    @PostMapping(path = "/",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCategory(@ModelAttribute @Valid CategoryRequestDto categoryRequest){
        Category category = categoryService.registerCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("/")
    public ResponseEntity<?> getCategory(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String keyword
            ){
        if(categoryName==null && keyword==null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.getAllCategories());
        }
        if(categoryName!=null){
            Category category = categoryService.getCategoryByName(categoryName)
                    .orElseThrow(()-> new ResourceNotFoundException("Category Not found for given name"));
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.ok(
                categoryService.getCategoryByKeyword(keyword)
        );
    }


    @Operation(summary = "Update category")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(
            path = "/{categoryId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateCategory(
            @PathVariable UUID categoryId,
            @ModelAttribute @Valid CategoryUpdateRequestDto request
    ) {

        Category updatedCategory =
                categoryService.updateCategory(categoryId, request);

        return ResponseEntity.ok(updatedCategory);
    }
}
