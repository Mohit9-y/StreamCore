package com.bingeForge.demo.dto.movie;

public record PlaybackResponse(
        String title,
        String manifestUrl,
        Integer durationInSeconds
) {}