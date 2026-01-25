package com.learnease.server.util.mappers;

import com.learnease.server.dto.wishlist.WishlistResponseDto;
import com.learnease.server.model.Wishlist;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistResponseDto toResponseDto(Wishlist wishlist) {

        return new WishlistResponseDto(
                wishlist.getCourse().getId(),
                wishlist.getCourse().getTitle(),
                wishlist.getCourse().getThumbnail(),
                wishlist.getCourse().getFees(),
                wishlist.getCourse().getDiscount()
        );
    }
}
