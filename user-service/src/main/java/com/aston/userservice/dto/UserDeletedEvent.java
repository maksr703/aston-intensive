package com.aston.userservice.dto;

import java.util.UUID;

public record UserDeletedEvent(UUID id, String email, String name) {
}
