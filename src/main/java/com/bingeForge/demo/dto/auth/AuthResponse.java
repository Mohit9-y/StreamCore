package com.bingeForge.demo.dto.auth;

import com.bingeForge.demo.dto.user.UserResponse;
import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String refreshToken,
        UserResponse user
) {}