package com.learnease.server.dto.wishlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDto {

    private UUID courseId;
    private String title;
    private String thumbnail;
    private double fees;
    private int discount;
}