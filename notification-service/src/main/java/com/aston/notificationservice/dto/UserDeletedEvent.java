package com.aston.notificationservice.dto;

import java.util.UUID;

public record UserDeletedEvent(
        UUID id,
        String email,
        String name
) {}