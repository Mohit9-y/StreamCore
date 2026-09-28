package com.bingeForge.demo.dto.premiumPlan;

import com.bingeForge.demo.enums.ScreenResolution;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record PremiumPlanRes(
        UUID id,
        String planName,
        BigDecimal price,
        Integer durationDays,
        String description,
        ScreenResolution maxScreenResolution,
        Integer maxConcurrentScreens,
        Boolean isAdSupported,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {}