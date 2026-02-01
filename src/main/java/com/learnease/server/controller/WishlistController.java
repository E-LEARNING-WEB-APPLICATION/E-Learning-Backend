package com.learnease.server.controller;


import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.wishlist.WishlistResponseDto;
import com.learnease.server.model.Wishlist;
import com.learnease.server.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
@Tag(name = "Wishlist", description = "APIs for managing student wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "Add course to wishlist")
    @PostMapping("/{courseId}")
    public ResponseEntity<ApiResponse> addToWishlist(
            @PathVariable UUID courseId,
            @AuthenticationPrincipal JWTDTO jwtdto
    ) {
        wishlistService.addToWishlist(jwtdto.getUserId(), courseId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(true, "Course added to wishlist"));
    }


    @Operation(summary = "Remove course from wishlist")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse> removeFromWishlist(
            @PathVariable UUID courseId,
            @AuthenticationPrincipal JWTDTO jwtdto
    ) {
        wishlistService.removeFromWishlist(jwtdto.getUserId(), courseId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(true, "Course removed from wishlist"));
    }

    @Operation(summary = "mark course purchased")
    @PatchMapping("/{courseId}/purchased")
    public ResponseEntity<ApiResponse> markCoursePurchased(
            @PathVariable UUID courseId,
            @AuthenticationPrincipal JWTDTO jwtdto
    ) {
        wishlistService.markCoursePurchased(jwtdto.getUserId(), courseId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(true, "Course removed from wishlist"));
    }

    @Operation(summary = "Get wishlist")
    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> getWishlist(
            @AuthenticationPrincipal JWTDTO jwtdto
    ) {
        List<WishlistResponseDto> wishlist =
                wishlistService.getWishlist(jwtdto.getUserId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wishlist);
    }

    @Operation(summary = "Get wishlist count")
    @GetMapping("/count")
    public ResponseEntity<Long> getWishlistCount(
            @AuthenticationPrincipal JWTDTO jwtdto
    ) {
        long count =
                wishlistService.getWishlistCount(jwtdto.getUserId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(count);
    }
}
