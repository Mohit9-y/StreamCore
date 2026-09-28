package com.bingeForge.demo.dto.user;

import java.util.UUID;

public record UpdateUserNameRequest(
        UUID id,
        String userName
) {}
