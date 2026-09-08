package com.bingeForge.demo.dto;

import java.util.UUID;

public record UpdateUserProfilePictureRequest(
        UUID id,
        String profilePicture
) {}
