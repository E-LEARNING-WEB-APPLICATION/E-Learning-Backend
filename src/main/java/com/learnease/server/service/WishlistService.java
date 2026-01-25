package com.learnease.server.service;

import com.learnease.server.dto.wishlist.WishlistResponseDto;

import java.util.List;
import java.util.UUID;

public interface WishlistService {
    void addToWishlist(UUID authId, UUID courseId);

    void removeFromWishlist(UUID authId, UUID courseId);

    List<WishlistResponseDto> getWishlist(UUID authId);

    long getWishlistCount(UUID authId);
}
