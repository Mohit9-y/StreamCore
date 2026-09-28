package com.bingeForge.demo.dto.history;

import com.bingeForge.demo.enums.ContentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;

public record WatchPositionRequest(
        @NotNull UUID contentId,
        @NotNull ContentType contentType,
        @PositiveOrZero Integer lastWatchedPositionSeconds,
        @PositiveOrZero Integer totalDurationSeconds
) {}