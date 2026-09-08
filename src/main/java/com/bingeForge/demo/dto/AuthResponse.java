package com.bingeForge.demo.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String refreshToken,
        UserResponse user
) {}