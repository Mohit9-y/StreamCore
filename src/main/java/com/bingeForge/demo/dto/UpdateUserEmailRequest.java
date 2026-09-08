package com.bingeForge.demo.dto;

import java.util.UUID;

public record UpdateUserEmailRequest(
        UUID id,
        String email
) {}
