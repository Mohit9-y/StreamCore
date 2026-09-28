package com.bingeForge.demo.dto.user;

import java.util.UUID;

public record UpdatePasswordRequest(
        UUID id,
        String oldPassword,
        String newPassword
) {}
