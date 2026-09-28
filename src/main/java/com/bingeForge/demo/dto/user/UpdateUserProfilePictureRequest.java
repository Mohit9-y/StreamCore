package com.bingeForge.demo.dto.user;

import java.util.UUID;

public record UpdateUserProfilePictureRequest(
        UUID id,
        String profilePicture
) {}
