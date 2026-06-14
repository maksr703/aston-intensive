package com.aston.userservice.dto;

import java.util.UUID;

public record UserCreatedEvent(UUID id, String email, String name) {
}
