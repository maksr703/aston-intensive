package com.aston.userservice.dto;

import java.util.UUID;

public record UserResponse (UUID id, String email, String name, Integer age) {}