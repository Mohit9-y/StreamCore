package com.bingeForge.demo.dto;

import java.util.UUID;

public record UpdateUserNameRequest(
        UUID id,
        String userName
) {}
