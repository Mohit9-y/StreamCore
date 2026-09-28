package com.bingeForge.demo.dto.user;

import java.util.UUID;

public record UpdateUserEmailRequest(
        UUID id,
        String email
) {}
