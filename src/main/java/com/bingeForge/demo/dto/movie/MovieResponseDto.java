package com.bingeForge.demo.dto.movie;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record MovieResponseDto(
        UUID id,
        String videoUrl,
        String title,
        String description,
        String thumbnailUrl,
        String trailerUrl,
        Integer durationInSeconds,
        Integer releaseYear,
        String castMembers,
        Boolean isPremium,
        BigDecimal rentAmount,
        Instant createdAt
) {
    // Compact constructor to handle the default fallback for rentAmount
    public MovieResponseDto {
        if (rentAmount == null) {
            rentAmount = BigDecimal.ZERO;
        }
    }
}