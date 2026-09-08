package com.bingeForge.demo.dto;

import com.bingeForge.demo.enums.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String email,
        String profilePicture,
        UserRole role // Include if the client needs to know the user's role
) {}