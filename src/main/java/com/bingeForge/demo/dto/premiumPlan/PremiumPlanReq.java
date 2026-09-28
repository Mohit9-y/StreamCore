package com.bingeForge.demo.dto.premiumPlan;

import com.bingeForge.demo.enums.ScreenResolution;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PremiumPlanReq(
        @NotBlank(message = "Plan name is required")
        @Size(max = 100, message = "Plan name must not exceed 100 characters")
        String planName,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price cannot be negative")
        @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 decimal places")
        BigDecimal price,

        @NotNull(message = "Duration in days is required")
        @Positive(message = "Duration must be greater than 0 days")
        Integer durationDays,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotNull(message = "Screen resolution is required")
        ScreenResolution maxScreenResolution,

        @Positive(message = "Concurrent screens must be at least 1")
        Integer maxConcurrentScreens,

        Boolean isAdSupported,

        Boolean isActive
) {
    public PremiumPlanReq {
        if (maxConcurrentScreens == null) {
            maxConcurrentScreens = 1;
        }
        if (isAdSupported == null) {
            isAdSupported = false;
        }
        if (isActive == null) {
            isActive = true;
        }
    }
}
