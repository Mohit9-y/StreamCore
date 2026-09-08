package com.bingeForge.demo.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record MovieRequestDto(
        @NotBlank(message = "Video URL is required")
        String videoUrl,

        @NotBlank(message = "Title is required")
        String title,

        String description,
        String thumbnailUrl,
        String trailerUrl,

        @Positive(message = "Duration must be greater than 0")
        Integer durationInSeconds,

        Integer releaseYear,
        String castMembers,
        Boolean isPremium,

        @Digits(integer = 8, fraction = 2, message = "Rent amount must have up to 8 integer digits and 2 decimals")
        @PositiveOrZero(message = "Rent amount cannot be negative")
        BigDecimal rentAmount
) {}